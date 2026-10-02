import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture

from jwk_test_values import jwk_properties


# <1>
@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False, properties=jwk_properties()),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_when_you_define_a_bean_of_type_jwk_provider_then_the_keys_endpoint_is_exposed(client):
    response = client.get("/keys")
    assert response.status_code == 200
    body = response.json()
    assert [key["kid"] for key in body["keys"]] == [
        "e3be37177a7c42bcbadd7cc63715f216",
        "0794e938379540dc8eaa559508524a79",
    ]
