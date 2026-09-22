import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def secured_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def open_context(request):
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
def secured_client(secured_context):
    return requests.with_context(secured_context)


@pytest.fixture
def open_client(open_context):
    return requests.with_context(open_context)


def test_books_controller_is_secured(secured_client):
    response = secured_client.get("/books")
    assert response.status_code == 401


def test_retrieve_books(open_client):
    response = open_client.get("/books")
    assert response.status_code == 200, response.text

    books = response.json()
    assert len(books) == 3
    assert {"isbn": "1491950358", "name": "Building Microservices"} in books
    assert {"isbn": "1680502395", "name": "Release It!"} in books


def test_health_endpoint_exposed(secured_client):
    response = secured_client.get("/health")
    assert response.status_code == 200, response.text
