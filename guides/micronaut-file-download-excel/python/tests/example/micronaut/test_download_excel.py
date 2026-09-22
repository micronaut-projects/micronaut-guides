import pytest
import requests

from builders.dsl.spreadsheet.query.poi import PoiSpreadsheetCriteria
from java.io import ByteArrayInputStream
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

    input_stream = ByteArrayInputStream(response.content)  # <4>
    query = PoiSpreadsheetCriteria.FACTORY.forStream(input_stream)
    result = query.query(
        lambda workbook: workbook.sheet(
            "Books",
            lambda sheet: sheet.row(
                lambda row: row.cell(
                    lambda cell: cell.value("Building Microservices")
                )
            ),
        )
    )

    assert result.getCells().size() == 1
