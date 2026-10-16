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


@pytest.fixture
def user_repository(my_context):
    return my_context["example.micronaut.UserJdbcRepository"]


def test_login_without_credentials_returns_bad_request(client):
    response = client.post("/login", headers={"Accept": "application/json"})

    assert response.status_code == 400


def test_login_with_valid_database_user_returns_access_token(client, user_repository):
    assert user_repository.count() > 0

    response = client.post(
        "/login",
        json={"username": "sherlock", "password": "elementary"},
    )

    assert response.status_code == 200
    body = response.json()
    assert body["username"] == "sherlock"
    assert body["access_token"] is not None
    assert isinstance(JWTParser.parse(body["access_token"]), SignedJWT)
