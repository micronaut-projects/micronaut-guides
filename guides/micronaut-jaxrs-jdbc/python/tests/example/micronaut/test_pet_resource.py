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


@pytest.fixture
def pet_repository(my_context):
    return my_context["example.micronaut.PetRepository"]


@pytest.fixture(autouse=True)
def clean_database(pet_repository):
    pet_repository.deleteAll()
    yield
    pet_repository.deleteAll()


def test_all(client, my_context):
    router = my_context["io.micronaut.web.router.Router"]
    pet_routes = [
        (
            route.getHttpMethodName(),
            str(route.getUriMatchTemplate()),
            str(route.getTargetMethod()),
        )
        for route in router.uriRoutes().toList()
        if str(route.getUriMatchTemplate()).startswith("/pets")
    ]
    assert any(
        method == "GET" and uri == "/pets" and target.endswith(" all()")
        for method, uri, target in pet_routes
    ), pet_routes
    assert any(
        method == "GET" and uri == "/pets/{name}" and " by_name(" in target
        for method, uri, target in pet_routes
    ), pet_routes
    assert any(
        method == "POST" and uri == "/pets" and " save(" in target
        for method, uri, target in pet_routes
    ), pet_routes

    for pet in (
        {"name": "Dino", "type": "DOG"},
        {"name": "Baby Puss", "type": "CAT"},
        {"name": "Hoppy", "type": "DOG"},
    ):
        response = client.post("/pets", json=pet)
        assert response.status_code == 201, response.text

    response = client.get("/pets")
    assert response.status_code == 200, response.text

    pet_names = response.json()
    assert len(pet_names) == 3


def test_get(client):
    response = client.post("/pets", json={"name": "Dino", "type": "DOG"})
    assert response.status_code == 201, response.text

    response = client.get("/pets/Dino")
    assert response.status_code == 200, response.text

    pet = response.json()
    assert pet["name"] == "Dino"
    assert pet["type"] == "DOG"
    assert pet["id"] is not None


def test_get_if_pet_does_not_exist_returns_not_found(client):
    response = client.post("/pets", json={"name": "Dino", "type": "DOG"})
    assert response.status_code == 201, response.text

    response = client.get("/pets/Foo")
    assert response.status_code == 404, response.text  # <3>


def test_save(client):
    response = client.get("/pets")
    assert response.status_code == 200, response.text
    old_count = len(response.json())

    response = client.post("/pets", json={"name": "Dino", "type": "DOG"})
    assert response.status_code == 201, response.text

    response = client.get("/pets")
    assert response.status_code == 200, response.text
    assert len(response.json()) == old_count + 1
