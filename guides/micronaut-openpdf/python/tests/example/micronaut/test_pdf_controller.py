from io import BytesIO

import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture
from pypdf import PdfReader


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),
    )  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    with requests.with_context(my_context) as session:  # <2>
        yield session


def test_download(client):
    response = client.get("/pdf/download")  # <3>

    assert response.status_code == 200
    assert response.headers["Content-Type"] == "application/pdf"
    assert response.headers["Content-Disposition"] == (
        "attachment; filename=example.pdf"
    )
    assert response.content.startswith(b"%PDF-")
    with BytesIO(response.content) as source:
        assert PdfReader(source).pages[0].extract_text() == "Hello World"
