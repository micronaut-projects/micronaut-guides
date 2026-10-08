import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture(scope="module")
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),
    )
    try:
        yield fixture
    finally:
        fixture.stop()


@pytest.fixture
def client(my_context):
    with requests.with_context(my_context) as session:
        yield session


def test_home_page_renders_anonymous_user(client):
    response = client.get("/", timeout=90)

    assert response.status_code == 200, response.text
    assert "username: Anonymous" in response.text


def test_anonymous_user_sees_provider_login(client):
    response = client.get("/", timeout=90)

    assert response.status_code == 200, response.text
    assert 'href="/oauth/login/cognito"' in response.text
    assert 'href="/oauth/logout"' not in response.text


def test_home_page_is_rendered_html(client):
    response = client.get("/", timeout=90)

    assert response.status_code == 200, response.text
    assert response.headers["Content-Type"].startswith("text/html")
    assert "{{" not in response.text
    assert "{%" not in response.text
