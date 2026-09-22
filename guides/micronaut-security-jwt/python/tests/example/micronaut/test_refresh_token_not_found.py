import pytest
import requests
from micronaut.security.authentication import Authentication
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(request, MicronautTest(transactional=False))
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_accessing_secured_url_without_authenticating_returns_unauthorized(my_context, client):
    refresh_token_generator = my_context["io.micronaut.security.token.generator.RefreshTokenGenerator"]
    user = Authentication.build("sherlock")

    refresh_token = refresh_token_generator.createKey(user)
    refresh_token_optional = refresh_token_generator.generate(user, refresh_token)
    assert refresh_token_optional.isPresent()

    signed_refresh_token = refresh_token_optional.get()  # <1>
    response = client.post(
        "/oauth/access_token",
        json={"grant_type": "refresh_token", "refresh_token": signed_refresh_token},
    )

    assert response.status_code == 400
    body = response.json()
    assert body["error"] == "invalid_grant"
    assert body["error_description"] == "refresh token not found"
