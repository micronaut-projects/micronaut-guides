import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(request, MicronautTest(environments=["test"], transactional=False))
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_index(client):
    response = client.get("/")

    assert response.status_code == 200
    assert response.text == "Hello World"


def test_hello(client):
    response = client.get("/hello/Micronaut")

    assert response.status_code == 200
    assert response.json() == {"message": "Hello Micronaut!"}
