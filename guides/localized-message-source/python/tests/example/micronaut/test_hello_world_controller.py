import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(request, MicronautTest(transactional=False))  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    with requests.with_context(my_context) as session:  # <2>
        yield session


def test_use_of_localized_message_source(client):
    response = client.get("/", headers={"Accept-Language": "es"})  # <3>
    assert response.status_code == 200
    assert response.text == "Hola Mundo"

    response = client.get("/")
    assert response.status_code == 200
    assert response.text == "Hello World"
