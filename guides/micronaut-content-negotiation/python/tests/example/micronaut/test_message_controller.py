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
    with requests.with_context(my_context) as session:  # <2>
        yield session


def test_content_negotiation(client):
    response = client.get("/", headers={"Accept": "application/json"})  # <3>

    assert response.status_code == 200
    assert response.headers["content-type"].startswith("application/json")
    assert response.json() == {"message": "Hello World"}

    response = client.get("/", headers={"Accept": "text/html"})

    assert response.status_code == 200
    assert response.headers["content-type"].startswith("text/html")
    assert response.text == """<!DOCTYPE html>
<html lang="en">
<body>
<h1>Hello World</h1>
</body>
</html>
"""


def test_prefers_json_when_it_has_the_higher_quality(client):
    response = client.get(
        "/",
        headers={"Accept": "text/html;q=0.1, application/json;q=0.9"},
    )

    assert response.status_code == 200
    assert response.headers["content-type"].startswith("application/json")
    assert response.json() == {"message": "Hello World"}


def test_prefers_html_when_it_has_the_higher_quality(client):
    response = client.get(
        "/",
        headers={"Accept": "application/json;q=0.1, text/html;q=0.9"},
    )

    assert response.status_code == 200
    assert response.headers["content-type"].startswith("text/html")
    assert "<h1>Hello World</h1>" in response.text
