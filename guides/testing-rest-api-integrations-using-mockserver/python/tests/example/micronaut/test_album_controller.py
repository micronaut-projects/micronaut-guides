import json

import java
import pytest
import requests
from com.github.tomakehurst.wiremock import WireMockServer
from com.github.tomakehurst.wiremock.client import WireMock
from com.github.tomakehurst.wiremock.core import WireMockConfiguration
from pyronaut.test import MicronautTest, micronaut_test_fixture

PHOTO_RESPONSE = json.dumps([
    {
        "id": 1,
        "title": "accusamus beatae ad facilis cum similique qui sunt",
        "url": "https://via.placeholder.com/600/92c952",
        "thumbnailUrl": "https://via.placeholder.com/150/92c952",
    },
    {
        "id": 2,
        "title": "reprehenderit est deserunt velit ipsam",
        "url": "https://via.placeholder.com/600/771796",
        "thumbnailUrl": "https://via.placeholder.com/150/771796",
    },
])


@pytest.fixture
def wiremock_server():  # <1>
    server = WireMockServer(WireMockConfiguration.options().dynamicPort())
    server.start()
    try:
        yield server
    finally:
        server.stop()


@pytest.fixture
def app_context(request, wiremock_server):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={
                "micronaut.http.services.photosapi.url": wiremock_server.baseUrl(),  # <2>
            },
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(app_context):
    return requests.with_context(app_context)


def stub_photos(wiremock_server, album_id: int, status: int = 200, body: str = PHOTO_RESPONSE):
    response = WireMock.aResponse().withStatus(status)
    if body is not None:
        response = response.withHeader("Content-Type", "application/json").withBody(body)
    wiremock_server.stubFor(  # <4>
        WireMock.get(WireMock.urlMatching(f"/albums/{album_id}/photos")).willReturn(response)
    )


def test_should_get_album_by_id(wiremock_server, client):  # <3>
    album_id = 1
    stub_photos(wiremock_server, album_id)

    response = client.get(f"/api/albums/{album_id}")  # <5>

    assert response.status_code == 200
    body = response.json()
    assert body["albumId"] == album_id
    assert len(body["photos"]) == 2


def test_should_return_server_error_when_photo_service_call_failed(wiremock_server, client):  # <6>
    album_id = 2
    stub_photos(wiremock_server, album_id, status=500, body=None)

    response = client.get(f"/api/albums/{album_id}")

    assert response.status_code == 500
