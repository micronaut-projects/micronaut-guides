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


def test_retrieve_books(client):
    response = client.get("/books")
    assert response.status_code == 200, response.text

    books = response.json()
    assert len(books) == 3
    assert {"isbn": "1491950358", "name": "Building Microservices"} in books
    assert {"isbn": "1680502395", "name": "Release It!"} in books


def test_health_endpoint_exposed(client):
    response = client.get("/health")
    assert response.status_code == 200, response.text
