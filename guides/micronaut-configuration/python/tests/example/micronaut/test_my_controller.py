import pytest

from pyronaut import requests
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
def client(my_context):
    with requests.with_context(my_context) as session:
        yield session


def test_my_team(client):
    response = client.get("/my/team")

    assert response.status_code == 200
    assert response.json() == {
        "name": "Steelers",
        "color": "Black",
        "player_names": ["Mason Rudolph", "James Connor"],
    }


def test_my_stadium(client):
    response = client.get("/my/stadium")

    assert response.status_code == 200
    assert response.json() == {"name": "pnc", "city": "Pittsburgh", "size": 35000}
