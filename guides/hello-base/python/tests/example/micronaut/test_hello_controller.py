import pytest
import requests

from micronaut.runtime.server import EmbeddedServer
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
def base_url(my_context) -> str:
    server = my_context[EmbeddedServer]
    return f"http://localhost:{server.getPort()}"


def test_hello_world_response(base_url: str):
    response = requests.get(f"{base_url}/hello")  # <2>

    assert response.text == "Hello World"  # <3>
