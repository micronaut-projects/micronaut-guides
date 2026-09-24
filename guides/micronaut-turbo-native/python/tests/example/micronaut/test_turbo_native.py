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
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_home_page_and_static_assets_render(client):
    response = client.get("/", headers={"Accept": "text/html"})

    assert response.status_code == 200
    assert "Turbo Native Demo" in response.text
    assert "/protected" in response.text

    response = client.get("/app.css")
    assert response.status_code == 200
    assert ".actions__icon" in response.text


def test_basic_and_reference_pages_render(client):
    for uri, text in [
        ("/one", "How’d You Get Here?"),
        ("/two?action=replace", "Push or Replace?"),
        ("/reference/turbo-native", "Turbo Native"),
    ]:
        response = client.get(uri, headers={"Accept": "text/html"})
        assert response.status_code == 200
        assert text in response.text


def test_redirects_and_not_found_status(client):
    response = client.get("/follow", allow_redirects=False)
    assert response.status_code == 307
    assert response.headers["Location"] == "/redirected"

    response = client.post("/new", data={}, allow_redirects=False)
    assert response.status_code == 302
    assert response.headers["Location"] == "/success"

    response = client.get("/nonexistent")
    assert response.status_code == 404


def test_sign_in_and_sign_out(client, my_context):
    response = client.get("/protected", headers={"Accept": "text/html"})
    assert response.status_code == 401

    response = client.post(
        "/login",
        files={"name": (None, "sherlock")},
        allow_redirects=False,
    )
    assert response.status_code in (302, 303)
    assert response.headers["Location"] == "/"

    response = client.get("/protected", headers={"Accept": "text/html"})
    assert response.status_code == 200
    assert "Protected Page" in response.text

    response = client.post("/signout", data={}, allow_redirects=False)
    assert response.status_code in (302, 303)
    assert response.headers["Location"] == "/"
