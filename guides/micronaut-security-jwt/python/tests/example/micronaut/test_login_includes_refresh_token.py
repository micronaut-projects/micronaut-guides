import pytest
import requests
from com.nimbusds.jwt import JWTParser, SignedJWT
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(request, MicronautTest(transactional=False))
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_upon_successful_authentication_user_gets_access_token_and_refresh_token(client):
    response = client.post("/login", json={"username": "sherlock", "password": "password"})

    assert response.status_code == 200
    body = response.json()
    assert body["username"] == "sherlock"
    assert body["access_token"] is not None
    assert body["refresh_token"] is not None  # <1>
    assert isinstance(JWTParser.parse(body["access_token"]), SignedJWT)
