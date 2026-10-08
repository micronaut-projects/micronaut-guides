import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),
    )  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    with requests.with_context(my_context) as session:
        yield session


def test_hello_world_response(client):
    response = client.get("/hello")  # <2>

    assert response.status_code == 200
    assert response.text == "Hello World"  # <3>
