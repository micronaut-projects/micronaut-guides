import pytest
import requests

from micronaut.runtime.server import EmbeddedServer
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
def base_url(my_context) -> str:
    server = my_context[EmbeddedServer]
    return f"http://localhost:{server.getPort()}"


@pytest.mark.parametrize("path", ["/constructor", "/field", "/setter"])
def test_message_controllers(base_url: str, path: str):
    response = requests.get(
        f"{base_url}{path}",
        headers={"Accept": "text/plain"},
    )

    assert response.status_code == 200
    assert response.text == "Hello World"
