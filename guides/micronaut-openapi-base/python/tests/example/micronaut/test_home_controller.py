import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            transactional=False,
            properties={"micronaut.http.client.follow-redirects": "false"},
        ),  # <1> <2>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <3>


def test_home_controller_is_hidden(client):
    response = client.get("/swagger/micronaut-guides-1.0.yml")

    assert response.status_code == 200
    assert "operationId: home" not in response.text  # <4>
