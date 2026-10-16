import pytest
import requests
from micronaut.security.token.jwt.validator import JsonWebTokenParser, JsonWebTokenSignatureValidator
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(request, MicronautTest(transactional=False))
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_upon_successful_authentication_user_gets_access_token_and_refresh_token(client, my_context):
    response = client.post("/login", json={"username": "sherlock", "password": "password"})

    assert response.status_code == 200
    body = response.json()
    assert body["username"] == "sherlock"
    assert body["access_token"] is not None
    assert body["refresh_token"] is not None  # <1>
    parsed = my_context[JsonWebTokenParser].parse(body["access_token"])
    assert parsed.isPresent()
    assert my_context[JsonWebTokenSignatureValidator].validateSignature(parsed.get())
