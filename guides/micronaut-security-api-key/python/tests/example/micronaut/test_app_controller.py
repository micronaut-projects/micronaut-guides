import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(  # <2>
        request,
        MicronautTest(
            properties={  # <1>
                "api-keys.companyA.key": "XXX",
                "api-keys.companyA.name": "John",
            }
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <3>


def test_app_is_secured(client):
    response = client.get("/app", headers={"Accept": "text/plain"})

    assert response.status_code == 401


def test_api_key_not_valid_for_top_secret(client):
    response = client.get(
        "/app",
        headers={
            "Accept": "text/plain",
            "X-API-KEY": "XXX",
        },
    )

    assert response.status_code == 401
