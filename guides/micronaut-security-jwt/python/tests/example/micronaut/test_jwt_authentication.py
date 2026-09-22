import pytest
import requests
from com.nimbusds.jwt import JWTParser, SignedJWT
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(request, MicronautTest(transactional=False))  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def assert_signed_jwt(token: str):
    assert isinstance(JWTParser.parse(token), SignedJWT)


def login(client):
    return client.post(
        "/login",
        json={"username": "sherlock", "password": "password"},  # <4>
    )


def test_accessing_a_secured_url_without_authenticating_returns_unauthorized(client):
    response = client.get("/", headers={"Accept": "text/plain"})  # <3>

    assert response.status_code == 401  # <3>


def test_upon_successful_authentication_a_json_web_token_is_issued_to_the_user(client):
    response = login(client)

    assert response.status_code == 200
    body = response.json()  # <5>
    assert body["username"] == "sherlock"
    assert body["access_token"] is not None
    assert_signed_jwt(body["access_token"])

    response = client.get(
        "/",
        headers={
            "Accept": "text/plain",
            "Authorization": f"Bearer {body['access_token']}",  # <6>
        },
    )

    assert response.status_code == 200
    assert response.text == "sherlock"  # <7>
