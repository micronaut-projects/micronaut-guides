import json
import time

import java
import pytest
import requests
from org.testcontainers.containers import MockServerContainer
from org.testcontainers.utility import DockerImageName
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


def mockserver_put(endpoint: str, path: str, payload: dict | None = None):
    last_error = None
    for _ in range(20):
        try:
            response = requests.put(
                f"{endpoint}{path}",
                data=json.dumps(payload) if payload is not None else None,
                headers={"Content-Type": "application/json"} if payload is not None else None,
            )
            if response.status_code in {200, 201, 202}:
                return response
            last_error = RuntimeError(
                f"MockServer returned {response.status_code}: {response.text}"
            )
        except Exception as error:
            last_error = error
        time.sleep(0.25)
    raise last_error


@pytest.fixture(scope="module")
def mockserver_container():  # <1>
    container = MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.15.0"))
    container.start()
    try:
        yield container
    finally:
        container.stop()


@pytest.fixture
def mockserver_client(mockserver_container):
    endpoint = mockserver_container.getEndpoint()
    mockserver_put(endpoint, "/mockserver/reset")
    return endpoint


@pytest.fixture
def app_context(request, mockserver_container):  # <2>
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={
                "micronaut.http.services.photosapi.url": mockserver_container.getEndpoint(),  # <3>
            },
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(app_context):
    return requests.with_context(app_context)


def test_should_get_album_by_id(mockserver_client, client):  # <4>
    album_id = 1
    mockserver_put(
        mockserver_client,
        "/mockserver/expectation",
        {
            "httpRequest": {
                "method": "GET",
                "path": f"/albums/{album_id}/photos",
            },
            "httpResponse": {
                "statusCode": 200,
                "headers": {
                    "Content-Type": ["application/json; charset=utf-8"],
                },
                "body": PHOTO_RESPONSE,
            },
        },
    )

    response = client.get(f"/api/albums/{album_id}")  # <5>

    assert response.status_code == 200
    body = response.json()
    assert body["albumId"] == album_id
    assert len(body["photos"]) == 2
    verify_response = mockserver_put(
        mockserver_client,
        "/mockserver/verify",
        {
            "httpRequest": {
                "method": "GET",
                "path": f"/albums/{album_id}/photos",
            },
            "times": {
                "atLeast": 1,
                "atMost": 1,
            },
        },
    )
    assert verify_response.status_code == 202
