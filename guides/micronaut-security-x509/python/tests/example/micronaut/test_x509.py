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


def test_client_cert(client):
    response = client.get("/", headers={"Accept": "text/plain"})  # <3>

    expected = "Hello myusername (X.509 cert issued by CN=micronaut.guide.x509)"
    assert response.text == expected  # <4>
