from io import BytesIO

import pytest
import requests
from micronaut.http import HttpHeaders, MediaType
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
    session = requests.with_context(my_context)  # <2>
    yield session
    session.close()


def text_at_page(pdf_bytes: bytes, page_number: int) -> str:
    with BytesIO(pdf_bytes) as source:
        reader = PdfReader(source)
        return reader.pages[page_number - 1].extract_text()


def test_download(client):
    response = client.get("/pdf/download")  # <3>

    assert response.status_code == 200
    assert response.headers[HttpHeaders.CONTENT_TYPE] == str(MediaType.APPLICATION_PDF)
    assert response.headers[HttpHeaders.CONTENT_DISPOSITION] == (
        "attachment; filename=example.pdf"
    )
    assert response.content.startswith(b"%PDF-")
    assert text_at_page(response.content, 1) == "Hello World"
