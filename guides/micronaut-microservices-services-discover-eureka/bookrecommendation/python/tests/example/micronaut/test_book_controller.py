import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.test_book_inventory_client_stub import BookInventoryClientStub


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


@pytest.fixture
def error_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={"spec.name": "BookControllerErrorTest"},
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def error_client(error_context):
    return requests.with_context(error_context)


def test_retrieve_books(client, my_context):
    inventory_stub = my_context[BookInventoryClientStub]
    inventory_stub.in_flight = 0
    inventory_stub.max_in_flight = 0

    response = client.get("/books")
    assert response.status_code == 200, response.text
    assert response.json() == [{"name": "Building Microservices"}]
    assert inventory_stub.max_in_flight == 3


def test_inventory_errors_fail_the_request(error_client):
    response = error_client.get("/books")
    assert response.status_code == 500, response.text


def test_health_endpoint_exposed(client):
    response = client.get("/health")
    assert response.status_code == 200, response.text
