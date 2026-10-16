import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture(scope="module")
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_home_page_renders_anonymous_user(client):
    response = client.get("/", timeout=90)

    assert response.status_code == 200
    assert "Username: Anonymous" in response.text


def test_secure_page_rejects_anonymous_user(client):
    response = client.get(
        "/secure",
        allow_redirects=False,
        headers={"Accept": "text/html"},
        timeout=90,
    )

    assert response.status_code == 303
    assert response.headers["location"] == "/"
