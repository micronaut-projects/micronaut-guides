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
def fruit_repository(my_context):
    return my_context["example.micronaut.FruitRepository"]


@pytest.fixture(autouse=True)
def clean_database(fruit_repository):
    fruit_repository.deleteAll()
    yield
    fruit_repository.deleteAll()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_empty_database_contains_no_fruit(client):
    response = client.get("/fruits")

    assert response.status_code == 200
    assert response.json() == []


def test_interaction_with_the_controller(client):
    response = client.post("/fruits", json={"name": "banana"})
    assert response.status_code == 201
    banana = response.json()

    response = client.get("/fruits")
    assert response.status_code == 200
    fruits = response.json()
    assert len(fruits) == 1
    assert fruits[0]["name"] == banana["name"]
    assert fruits[0].get("description") is None

    response = client.post(
        "/fruits",
        json={"name": "apple", "description": "Keeps the doctor away"},
    )
    assert response.status_code == 201

    response = client.get("/fruits")
    assert response.status_code == 200
    fruits = response.json()
    assert any(fruit.get("description") == "Keeps the doctor away" for fruit in fruits)

    banana["description"] = "Yellow and curved"
    response = client.put("/fruits", json=banana)
    assert response.status_code == 200

    response = client.get("/fruits")
    assert response.status_code == 200
    descriptions = {fruit.get("description") for fruit in response.json()}
    assert descriptions == {"Keeps the doctor away", "Yellow and curved"}


def test_search_works_as_expected(client):
    client.post("/fruits", json={"name": "apple", "description": "Keeps the doctor away"})
    client.post("/fruits", json={"name": "pineapple", "description": "Delicious"})
    client.post("/fruits", json={"name": "lemon", "description": "Lemonentary my dear Dr Watson"})

    response = client.get(
        "/fruits/q",
        params=[("names", "apple"), ("names", "pineapple")],
    )

    assert response.status_code == 200
    names = {fruit["name"] for fruit in response.json()}
    assert names == {"apple", "pineapple"}
