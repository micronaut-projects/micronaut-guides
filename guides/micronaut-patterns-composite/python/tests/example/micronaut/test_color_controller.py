import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def test_composite_pattern(client):
    response = client.get("/color", headers={"color": "yellow"})

    assert response.status_code == 200
    assert response.text == "yellow"

    response = client.get("/color")

    assert response.status_code == 404

    response = client.get("/color/mint", headers={"color": "yellow"})

    assert response.status_code == 200
    assert response.text == "yellow"

    response = client.get("/color/mint")

    assert response.status_code == 200
    assert response.text == "mint"
