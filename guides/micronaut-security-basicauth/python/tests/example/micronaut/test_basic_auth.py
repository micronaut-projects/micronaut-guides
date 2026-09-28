import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(request, MicronautTest())  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def test_verify_http_basic_auth_works(client):
    response = client.get("/", headers={"Accept": "text/plain"})  # <3>

    assert response.status_code == 401, response.text  # <4>
    assert response.headers["WWW-Authenticate"] == 'Basic realm="Micronaut Guide"'

    response = client.get(
        "/",
        headers={"Accept": "text/plain"},
        auth=("sherlock", "password"),  # <5>
    )

    assert response.status_code == 200
    assert response.text == "sherlock"  # <6>
