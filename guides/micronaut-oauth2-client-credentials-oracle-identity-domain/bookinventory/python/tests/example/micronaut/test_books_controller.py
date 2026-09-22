import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            properties={"micronaut.security.enabled": "false"},
            transactional=False,
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_books_controller(client):
    response = client.get("/books/stock/1491950358")
    assert response.status_code == 200, response.text
    assert response.text == "true"


def test_books_controller_with_non_existing_isbn(client):
    response = client.get("/books/stock/XXXXX")
    assert response.status_code == 404, response.text


def test_health_endpoint_exposed(client):
    response = client.get("/health")
    assert response.status_code == 200, response.text
