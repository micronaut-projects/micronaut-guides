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


def test_login_with_valid_credentials_returns_access_token(client):
    response = client.post(
        "/login",
        json={"username": "sherlock", "password": "elementary"},  # <3>
    )

    assert response.status_code == 200
    body = response.json()
    assert body["access_token"] is not None
    assert isinstance(JWTParser.parse(body["access_token"]), SignedJWT)  # <4>
