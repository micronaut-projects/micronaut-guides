from io import BytesIO

import pytest
import requests
from openpyxl import load_workbook
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),
    )  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def test_books_can_be_downloaded_as_an_excel_file(client):
    response = client.get(
        "/excel",
        headers={
            "Accept": "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        },
    )  # <3>

    assert response.status_code == 200
    assert response.headers["Content-Disposition"] == 'attachment; filename="books.xlsx"'

    workbook = load_workbook(BytesIO(response.content))  # <4>
    sheet = workbook["Books"]
    assert sheet["B2"].value == "Building Microservices"
