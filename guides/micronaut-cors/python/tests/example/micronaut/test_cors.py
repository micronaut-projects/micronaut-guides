import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    with requests.with_context(my_context) as session:
        yield session


def test_cors_allows_configured_origin(client):
    response = client.get(
        "/hello",
        headers={"Origin": "http://127.0.0.1:8000"},
    )

    assert response.status_code == 200
    assert response.text == "Hello World"
    assert response.headers["Access-Control-Allow-Origin"] == "http://127.0.0.1:8000"


def test_cors_does_not_allow_unconfigured_origin(client):
    response = client.get(
        "/hello",
        headers={"Origin": "http://127.0.0.1:8001"},
    )

    assert response.status_code == 200
    assert response.headers.get("Access-Control-Allow-Origin") != "http://127.0.0.1:8001"


def test_preflight_allows_configured_origin_method_and_headers(client):
    response = client.options(
        "/hello",
        headers={
            "Origin": "http://127.0.0.1:8000",
            "Access-Control-Request-Method": "GET",
            "Access-Control-Request-Headers": "X-Requested-With",
        },
    )

    assert response.status_code == 200
    assert response.headers["Access-Control-Allow-Origin"] == "http://127.0.0.1:8000"
    assert response.headers["Access-Control-Allow-Methods"] == "GET"
    assert response.headers["Access-Control-Allow-Headers"].lower() == "x-requested-with"
