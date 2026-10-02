import pytest
import requests

from micronaut.runtime.server import EmbeddedServer
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def base_url(my_context) -> str:
    server = my_context[EmbeddedServer]
    return f"http://localhost:{server.getPort()}"


def test_my_team(base_url: str):
    response = requests.get(f"{base_url}/my/team")

    assert response.status_code == 200
    assert response.json()["name"] == "Steelers"
    assert response.json()["color"] == "Black"
    assert response.json()["player_names"] == ["Mason Rudolph", "James Connor"]


def test_my_stadium(base_url: str):
    response = requests.get(f"{base_url}/my/stadium")

    assert response.status_code == 200
    assert response.json()["city"] == "Pittsburgh"
    assert response.json()["size"] == 35000
