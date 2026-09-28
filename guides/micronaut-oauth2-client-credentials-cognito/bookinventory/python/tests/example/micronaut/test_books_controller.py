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
    response = secured_client.get("/books/stock/1491950358")
    assert response.status_code == 401


def test_stock_for_available_book(open_client):
    response = open_client.get("/books/stock/1491950358")
    assert response.status_code == 200, response.text
    assert response.text == "true"


def test_books_controller_with_non_existing_isbn(open_client):
    response = open_client.get("/books/stock/XXXXX")
    assert response.status_code == 404, response.text


def test_health_endpoint_exposed(secured_client):
    response = secured_client.get("/health")
    assert response.status_code == 200, response.text
