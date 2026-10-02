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


def test_cookie_based_authentication_flow(client):
    response = client.get("/")

    assert response.status_code == 200
    assert "You are not logged in" in response.text
    assert 'href="/login/auth"' in response.text

    response = client.get("/login/auth")

    assert response.status_code == 200
    assert "<title>Login</title>" in response.text
    assert 'id="errors"' not in response.text

    response = client.post(
        "/login",
        data={"username": "foo", "password": "foo"},
    )

    assert response.status_code == 200
    assert "<title>Login Failed</title>" in response.text
    assert 'id="errors"' in response.text

    response = client.post(
        "/login",
        data={"username": "sherlock", "password": "password"},
        allow_redirects=False,
    )

    assert response.status_code == 303
    assert client.cookies

    response = client.get("/")

    assert response.status_code == 200
    assert "username: <span>sherlock</span>" in response.text

    response = client.get("/logout", allow_redirects=False)

    assert response.status_code == 303

    response = client.get("/")

    assert response.status_code == 200
    assert "You are not logged in" in response.text
