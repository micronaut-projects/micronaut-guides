import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


CONFERENCES = {
    "Greach",
    "GR8Conf EU",
    "Micronaut Summit",
    "Devoxx Belgium",
    "Oracle Code One",
    "CommitConf",
    "Codemotion Madrid",
}


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
    return requests.with_context(my_context)


def test_random_conference(client):
    response = client.get("/conferences/random")

    assert response.status_code == 200
    assert response.json()["name"] in CONFERENCES
