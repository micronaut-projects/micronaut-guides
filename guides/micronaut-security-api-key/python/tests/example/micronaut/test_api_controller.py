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
                "api-keys.companyB.key": "YYY",
                "api-keys.companyB.name": "Paul",
            }
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <3>


def create_request(client, api_key: str):
    return client.get(
        "/api",
        headers={
            "Accept": "text/plain",
            "X-API-KEY": api_key,
        },
    )


def test_api_is_secured(client):
    response = client.get("/api", headers={"Accept": "text/plain"})

    assert response.status_code == 401


def test_api_not_accessible_if_wrong_key(client):
    response = create_request(client, "ZZZ")

    assert response.status_code == 401


def test_api_is_accessible_with_an_api_key(client):
    response = create_request(client, "XXX")

    assert response.status_code == 200
    assert response.text == "Hello John"

    response = create_request(client, "YYY")

    assert response.status_code == 200
    assert response.text == "Hello Paul"
