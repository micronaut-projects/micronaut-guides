import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


@pytest.mark.parametrize("path", ["/constructor", "/field", "/setter"])
def test_message_controllers(client, path: str):
    response = client.get(
        path,
        headers={"Accept": "text/plain"},
    )

    assert response.status_code == 200
    assert response.text == "Hello World"
