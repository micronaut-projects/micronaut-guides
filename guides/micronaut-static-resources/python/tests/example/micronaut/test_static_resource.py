import pytest
import requests

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
def client(my_context):
    return requests.with_context(my_context)  # <2>


def test_stylesheet_exists(client):
    response = client.get("/css/style.css")

    assert response.status_code == 200
    assert "html, body {" in response.text


def test_image_exists(client):
    response = client.get("/images/micronaut_stacked_black.png")

    assert response.status_code == 200
    with open("config/static/images/micronaut_stacked_black.png", "rb") as image:
        assert len(response.content) == len(image.read())
