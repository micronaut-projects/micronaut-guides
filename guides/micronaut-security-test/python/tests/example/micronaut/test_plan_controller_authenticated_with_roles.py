import pytest
import requests

from jakarta.inject import Singleton
from micronaut.context.annotation import Requires
from micronaut.core.async_.publisher import Publishers
from micronaut.http import HttpRequest
from micronaut.security.authentication import Authentication
from micronaut.security.filters import AuthenticationFetcher
from java.util import List
from org.reactivestreams import Publisher
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(properties={"spec.name": "PlanControllerAuthenticatedWithRolesTest"}),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def test_plan_controller_with_authenticated_user_with_roles(client):
    response = client.get("/plan", headers={"Accept": "text/plain"})

    assert response.status_code == 200
    assert response.text == "Kill Sherlock Holmes and his companions"


@Requires(property="spec.name", value="PlanControllerAuthenticatedWithRolesTest")
@Singleton
class MockRolesAuthenticationFetcher(AuthenticationFetcher):

    def fetchAuthentication(self, request: HttpRequest) -> Publisher:
        return Publishers.just(Authentication.build("moriarty", List.of("ROLE_EVIL_MASTERMIND")))
