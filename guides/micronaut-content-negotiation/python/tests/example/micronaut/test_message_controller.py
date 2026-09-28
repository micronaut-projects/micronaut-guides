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
