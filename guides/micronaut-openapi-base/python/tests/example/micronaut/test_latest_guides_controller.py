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


def test_guides_endpoint(client):
    response = client.get("/latest/guides.json")

    assert response.status_code == 200
    assert "2018-05-23" in response.text
    assert {
        "language": "PYTHON",
        "buildTool": "PYRONAUT",
        "url": "https://guides.micronaut.io/latest/creating-your-first-micronaut-app-pyronaut-python.html",
    } in response.json()[0]["options"]
