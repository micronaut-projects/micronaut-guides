import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_global_not_found_error(client):
    response = client.get("/foo", headers={"Accept": "application/json"})

    assert response.status_code == 404
    assert response.json()["message"] == "Page Not Found"

    response = client.get("/foo", headers={"Accept": "text/html"})

    assert response.status_code == 200
    assert response.headers["content-type"].startswith("text/html")
    assert "<title>Not Found</title>" in response.text
    assert "<h1>NOT FOUND</h1>" in response.text


def test_form_validation_errors_are_rendered(client):
    response = client.get("/books/create")

    assert response.status_code == 200
    assert "<title>Create Book</title>" in response.text
    assert 'id="errors"' not in response.text

    response = client.post("/books/save", data={"title": "", "pages": "0"})

    assert 'name="title" value=""' in response.text
    assert 'name="pages" value="0"' in response.text
    assert "title must not be blank" in response.text
    assert "pages must be greater than 0" in response.text


def test_out_of_stock_exception_handler(client):
    response = client.get("/books/stock/1234")

    assert response.status_code == 200
    assert response.text == "0"
