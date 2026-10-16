import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def entity_id(response):
    path = "/genres/"
    value = response.headers.get("Location")
    assert value is not None
    return int(value[value.index(path) + len(path):])


def test_find_non_existing_genre_returns_404(client):
    response = client.get("/genres/99")

    assert response.status_code == 404


def test_genre_crud_operations(client):
    genre_ids = []

    response = client.post("/genres", json={"name": "DevOps"})  # <3>
    assert response.status_code == 201
    genre_ids.append(entity_id(response))

    response = client.post("/genres", json={"name": "Microservices"})  # <3>
    assert response.status_code == 201
    genre_id = entity_id(response)
    genre_ids.append(genre_id)

    response = client.get(f"/genres/{genre_id}")  # <4>
    assert response.status_code == 200
    genre = response.json()
    assert genre["name"] == "Microservices"

    response = client.put(
        "/genres",
        json={"id": genre_id, "name": "Micro-services"},
    )  # <5>
    assert response.status_code == 204

    response = client.get(f"/genres/{genre_id}")
    assert response.status_code == 200
    genre = response.json()
    assert genre["name"] == "Micro-services"

    response = client.get("/genres/list")
    assert response.status_code == 200
    genres = response.json()
    assert len(genres) == 2

    response = client.post("/genres/ex", json={"name": "Rollback"})  # <3>
    assert response.status_code == 204

    response = client.get("/genres/list")
    assert response.status_code == 200
    genres = response.json()
    assert len(genres) == 2

    response = client.get("/genres/list?size=1")
    assert response.status_code == 200
    genres = response.json()
    assert len(genres) == 1
    assert genres[0]["name"] == "DevOps"

    response = client.get("/genres/list?size=1&sort=name,desc")
    assert response.status_code == 200
    genres = response.json()
    assert len(genres) == 1
    assert genres[0]["name"] == "Micro-services"

    response = client.get("/genres/list?size=1&page=2")
    assert response.status_code == 200
    genres = response.json()
    assert len(genres) == 0

    for genre_id in genre_ids:
        response = client.delete(f"/genres/{genre_id}")
        assert response.status_code == 204
