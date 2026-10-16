import json

import pytest
from micronaut.context.env import Environment
from pyronaut import requests
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
def wiremock_server(app_context):  # <1>
    endpoint = app_context[Environment].getProperties("wiremock-stubs")["url"]
    requests.delete(f"{endpoint}/__admin/mappings").raise_for_status()
    return endpoint


@pytest.fixture
def app_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={
                "micronaut.http.services.photosapi.url": "${wiremock-stubs.url}",  # <2>
            },
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(app_context):
    return requests.with_context(app_context)


def stub_photos(wiremock_server, album_id: int, status: int = 200, body: str = PHOTO_RESPONSE):
    response = {"status": status}
    if body is not None:
        response.update(headers={"Content-Type": "application/json"}, body=body)
    result = requests.post(  # <4>
        f"{wiremock_server}/__admin/mappings",
        json={
            "request": {"method": "GET", "url": f"/albums/{album_id}/photos"},
            "response": response,
        },
    )
    assert result.status_code == 201


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
