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


def test_stimulus_via_webjars_available(client):
    response = client.get("/webjars/hotwired__stimulus/3.2.1/dist/stimulus.js")

    assert response.status_code == 200
    assert "class Application" in response.text
