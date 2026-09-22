import base64

import pytest
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(request, MicronautTest())  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def app_client(my_context):
    return my_context["example.micronaut.AppClient"]  # <2>


def test_verify_basic_auth_works(app_client):
    credentials = basic_auth("sherlock", "password")
    response = app_client.home(credentials)  # <3>

    assert response == "sherlock"


def basic_auth(username: str, password: str) -> str:
    encoded = base64.b64encode(f"{username}:{password}".encode()).decode()
    return f"Basic {encoded}"
