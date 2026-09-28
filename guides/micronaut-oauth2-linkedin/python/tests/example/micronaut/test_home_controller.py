import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture(scope="module")
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


def test_home_page_renders_anonymous_user(client):
    response = client.get("/", timeout=90)

    assert response.status_code == 200, response.text
    assert "username: Anonymous" in response.text


def test_login_route_requires_authentication(client):
    response = client.get("/oauth/login/linkedin", allow_redirects=False, timeout=90)

    assert response.status_code == 401, response.text
