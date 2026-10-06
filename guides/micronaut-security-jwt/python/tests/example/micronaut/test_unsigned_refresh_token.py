import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(request, MicronautTest(transactional=False))
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_accessing_secured_url_without_authenticating_returns_unauthorized(client):
    unsigned_refresh_token = "foo"  # <1>

    response = client.post(
        "/oauth/access_token",
        json={"grant_type": "refresh_token", "refresh_token": unsigned_refresh_token},
    )

    assert response.status_code == 400
    body = response.json()
    assert body["error"] == "invalid_grant"
    assert body["error_description"] == "Refresh token is invalid"
