import time

import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(request, MicronautTest(transactional=False))
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_verify_jwt_access_token_refresh_works(my_context, client):
    refresh_token_repository = my_context["example.micronaut.RefreshTokenRepository"]

    old_token_count = refresh_token_repository.count()
    response = client.post("/login", json={"username": "sherlock", "password": "password"})
    time.sleep(3)
    assert refresh_token_repository.count() == old_token_count + 1

    assert response.status_code == 200
    body = response.json()
    assert body["access_token"] is not None
    assert body["refresh_token"] is not None

    time.sleep(1)
    refresh_response = client.post(
        "/oauth/access_token",
        json={"grant_type": "refresh_token", "refresh_token": body["refresh_token"]},  # <1>
    )

    assert refresh_response.status_code == 200
    refresh_body = refresh_response.json()
    assert refresh_body["access_token"] is not None
    assert refresh_body["access_token"] != body["access_token"]  # <2>

    refresh_token_repository.deleteAll()
