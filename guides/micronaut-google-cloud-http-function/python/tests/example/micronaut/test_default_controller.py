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


def test_default_get(client):
    response = client.get("/default")

    assert response.status_code == 200
    assert response.text == "Example Response"


def test_default_post(client):
    response = client.post("/default", json={"name": "Test Name"})

    assert response.status_code == 200
    assert response.json()["message"] == (
        "Hello Test Name, thank you for sending the message"
    )
