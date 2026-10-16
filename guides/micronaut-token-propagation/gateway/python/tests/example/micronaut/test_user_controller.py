import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(request, MicronautTest(transactional=False))  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def login(client):
    return client.post(
        "/login",
        json={"username": "sherlock", "password": "password"},
    )


def test_user_endpoint_is_secured(client):  # <3>
    response = client.get("/user")

    assert response.status_code == 401


def test_authenticated_can_fetch_username(client):
    response = login(client)

    assert response.status_code == 200
    body = response.json()

    response = client.get(
        "/user",
        headers={"Authorization": f"Bearer {body['access_token']}"},
    )

    assert response.status_code == 200
    assert response.text == "sherlock"
