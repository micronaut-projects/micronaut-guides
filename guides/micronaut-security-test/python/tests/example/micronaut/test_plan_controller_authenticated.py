import pytest
import requests

from jakarta.inject import Singleton
from micronaut.context.annotation import Requires
from micronaut.core.async_.publisher import Publishers
from micronaut.http import HttpRequest
from micronaut.security.authentication import Authentication
from micronaut.security.filters import AuthenticationFetcher
from org.reactivestreams import Publisher
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(properties={"spec.name": "PlanControllerAuthenticatedTest"}),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def test_plan_controller_with_authenticated_user(client):
    response = client.get("/plan", headers={"Accept": "text/plain"})

    assert response.status_code == 200
    assert response.text == "Plan New Year"


@Requires(property="spec.name", value="PlanControllerAuthenticatedTest")
@Singleton
class MockAuthenticatedAuthenticationFetcher(AuthenticationFetcher):

    def fetchAuthentication(self, request: HttpRequest) -> Publisher:
        return Publishers.just(Authentication.build("watson"))
