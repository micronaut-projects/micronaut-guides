import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.greeting import Greeting


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_greeting_service(client):
    response = client.get("/greeting", params={"name": "John"})

    assert response.status_code == 200
    assert response.json()["content"] == "Hola, John!"


def test_post_with_request_body_and_post_mapping_works(client):
    request = Greeting(99, "Sergio")

    response = client.post("/greeting", json=request.__dict__)

    assert response.status_code == 200
    body = response.json()
    assert body["id"] != request.id
    assert body["content"] == f"Hola, {request.content}!"


def test_delete_response_headers(client):
    response = client.delete("/greeting")

    assert response.status_code == 204
    assert response.headers["Foo"] == "Bar"


def test_response_status(client):
    response = client.get("/greeting-status", params={"name": "John"})

    assert response.status_code == 201
    assert response.json()["content"] == "Hola, John!"
