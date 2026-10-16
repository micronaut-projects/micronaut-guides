import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={"micronaut.http.client.follow-redirects": "false"},
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def location_id(response) -> int:
    location = response.headers.get("Location")
    assert location is not None
    return int(location.rsplit("/", 1)[1])


def test_root_redirects_to_rooms(client):
    response = client.get("/", allow_redirects=False)

    assert response.status_code == 303
    assert response.headers.get("Location") == "/rooms"


def test_assets_are_served(client):
    for resource in (
        "/assets/stylesheets/application.css",
        "/assets/stylesheets/scaffolds.css",
        "/assets/javascripts/controllers/reset_form_controller.mjs",
    ):
        response = client.get(resource)
        assert response.status_code == 200


def test_room_and_turbo_message_flow(client):
    response = client.post("/rooms", data={"name": "Room A"}, allow_redirects=False)
    assert response.status_code == 303
    room_id = location_id(response)

    try:
        response = client.get(f"/rooms/{room_id}")
        assert response.status_code == 200
        assert "/chat/" in response.text

        response = client.post(
            f"/rooms/{room_id}/messages",
            data={"content": "Hola"},
            allow_redirects=False,
        )
        assert response.status_code == 303

        response = client.get(f"/rooms/{room_id}")
        assert response.status_code == 200
        assert "Room A" in response.text
        assert "Hola" in response.text
    finally:
        client.post(f"/rooms/{room_id}/delete", data={}, allow_redirects=False)
