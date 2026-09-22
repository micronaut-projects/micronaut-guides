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


def test_cors_allows_configured_origin(base_url: str):
    response = requests.get(
        f"{base_url}/hello",
        headers={"Origin": "http://127.0.0.1:8000"},
    )

    assert response.status_code == 200
    assert response.text == "Hello World"
    assert response.headers["Access-Control-Allow-Origin"] == "http://127.0.0.1:8000"


def test_cors_does_not_allow_unconfigured_origin(base_url: str):
    response = requests.get(
        f"{base_url}/hello",
        headers={"Origin": "http://127.0.0.1:8001"},
    )

    assert response.status_code == 200
    assert response.headers.get("Access-Control-Allow-Origin") != "http://127.0.0.1:8001"
