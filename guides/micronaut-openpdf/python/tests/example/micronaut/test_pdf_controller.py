import pytest
import requests
from com.lowagie.text.pdf import PdfReader
from com.lowagie.text.pdf.parser import PdfTextExtractor
from java.io import ByteArrayInputStream
from micronaut.http import HttpHeaders, MediaType
from pyronaut.test import MicronautTest, micronaut_test_fixture


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
    return requests.with_context(my_context)  # <2>


def text_at_page(pdf_bytes: bytes, page_number: int) -> str:
    reader = PdfReader(ByteArrayInputStream(pdf_bytes))
    try:
        return PdfTextExtractor(reader).getTextFromPage(page_number)
    finally:
        reader.close()


def test_download(client):
    response = client.get("/pdf/download")  # <3>

    assert response.status_code == 200
    assert response.headers[HttpHeaders.CONTENT_TYPE] == str(MediaType.APPLICATION_PDF)
    assert response.headers[HttpHeaders.CONTENT_DISPOSITION] == (
        "attachment; filename=example.pdf"
    )
    assert text_at_page(response.content, 1) == "Hello World"
