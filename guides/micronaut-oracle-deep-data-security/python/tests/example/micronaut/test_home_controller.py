import pytest
import requests
from jakarta.inject import Singleton
from micronaut.context.annotation import Requires
from micronaut.http import HttpRequest
from micronaut.security.authentication import (
    AuthenticationFailureReason,
    AuthenticationRequest,
    AuthenticationResponse,
)
from micronaut.security.authentication.provider import HttpRequestAuthenticationProvider
from pyronaut.test import MicronautTest, Sql, micronaut_test_fixture

USERNAME = "emma@supremo.onmicrosoft.com"
PASSWORD = "secret"
BROWSER_ACCEPT = {"Accept": "text/html"}


@pytest.fixture
def server_context(request, oracle):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            properties={**oracle, "spec.name": "HomeControllerTest", "micronaut.server.port": "-1"},  # <2>
            sql=Sql("classpath:employees-test-data.sql"),
        ),
    )  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(server_context):
    return requests.with_context(server_context)


def test_anonymous_users_are_invited_to_sign_in(client):
    response = client.get("/", headers=BROWSER_ACCEPT)
    assert response.status_code == 200, response.text
    assert "Anonymous" in response.text
    assert "Sign in with Microsoft Entra ID" in response.text
    assert 'href="/oauth/login/entraid"' in response.text  # <3>


def test_signed_in_users_see_employees_and_salary_summary(client):
    response = client.get("/", headers=BROWSER_ACCEPT, auth=(USERNAME, PASSWORD))  # <4>
    assert response.status_code == 200, response.text
    page = response.text
    assert f'Signed in as <span class="fw-semibold">{USERNAME}</span>' in page
    assert 'href="/logout"' in page
    assert "9826.00" in page  # <5>
    for first_name in ("Victoria", "Marvin", "Chris", "Emma", "Taylor"):
        assert first_name in page, first_name  # <6>


@Requires(property="spec.name", value="HomeControllerTest")  # <7>
@Singleton
class BasicAuthProvider(HttpRequestAuthenticationProvider):  # <8>
    def authenticate(self, request_context: HttpRequest, auth_request: AuthenticationRequest) -> AuthenticationResponse:
        if USERNAME == str(auth_request.getIdentity()) and PASSWORD == str(auth_request.getSecret()):
            return AuthenticationResponse.success(USERNAME, {"preferred_username": USERNAME})  # <9>
        return AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)
