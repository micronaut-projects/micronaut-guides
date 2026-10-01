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
        ),
    )  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def test_home_page_renders_anonymous_user(client):
    response = client.get("/")  # <3>

    assert response.status_code == 200
    assert "You are not logged in" in response.text
    assert "/login/auth" in response.text


def test_login_form_renders(client):
    response = client.get("/login/auth")

    assert response.status_code == 200
    assert "Username" in response.text
    assert "Password" in response.text


def test_failed_login_redirects_to_auth_failed(client):
    response = client.post(
        "/login",
        data={"username": "sherlock", "password": "wrong"},
        allow_redirects=False,
    )  # <4>

    assert response.status_code == 303
    assert response.headers["Location"] == "/login/authFailed"

    response = client.get("/login/authFailed")
    assert response.status_code == 200
    assert "Login Failed" in response.text


def test_successful_login_and_logout(client):
    response = client.post(
        "/login",
        data={"username": "sherlock", "password": "password"},
        allow_redirects=False,
    )  # <5>

    assert response.status_code == 303
    assert response.headers["Location"] == "/"

    response = client.get("/")  # <6>
    assert response.status_code == 200
    assert "username: <span>sherlock</span>" in response.text

    response = client.post("/logout", data={}, allow_redirects=False)  # <7>
    assert response.status_code == 303
    assert response.headers["Location"] == "/"

    response = client.get("/")
    assert response.status_code == 200
    assert "You are not logged in" in response.text
