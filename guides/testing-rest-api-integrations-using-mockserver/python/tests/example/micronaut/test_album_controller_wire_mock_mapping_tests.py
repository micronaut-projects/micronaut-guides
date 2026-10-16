import pytest
from pyronaut import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture


# tag::registerExtension[]
@pytest.fixture
def app_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={
                "micronaut.http.services.photosapi.url": "${wiremock-mappings.url}",
            },
        ),
    )
    yield fixture
    fixture.stop()
# end::registerExtension[]


@pytest.fixture
def client(app_context):
    return requests.with_context(app_context)


# tag::shouldGetAlbumById[]
def test_should_get_album_by_id(client):
    album_id = 1
    response = client.get(f"/api/albums/{album_id}")

    assert response.status_code == 200
    body = response.json()
    assert body["albumId"] == album_id
    assert len(body["photos"]) == 2
# end::shouldGetAlbumById[]


def test_should_return_server_error_when_photo_service_call_failed(client):
    album_id = 2
    response = client.get(f"/api/albums/{album_id}")

    assert response.status_code == 500


def test_should_return_empty_photos(client):
    album_id = 3
    response = client.get(f"/api/albums/{album_id}")

    assert response.status_code == 200
    body = response.json()
    assert body["albumId"] == album_id
    assert body.get("photos", []) == []
