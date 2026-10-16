import pytest
import requests
from org.bson import Document
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.mongo_db_configuration import MongoDbConfiguration


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
    return requests.with_context(my_context)


@pytest.fixture(autouse=True)
def clean_database(my_context):
    mongo_conf = my_context[MongoDbConfiguration]
    mongo_client = my_context["com.mongodb.client.MongoClient"]
    collection = mongo_client.getDatabase(mongo_conf.name).getCollection(mongo_conf.collection)
    collection.deleteMany(Document())
    yield
    collection.deleteMany(Document())


def test_fruits_endpoint_interacts_with_mongo(client):
    response = client.get("/fruits")
    assert response.status_code == 200
    assert response.json() == []

    response = client.post("/fruits", json={"name": "banana"})
    assert response.status_code == 201

    response = client.get("/fruits")
    assert response.status_code == 200
    fruits = response.json()
    assert len(fruits) == 1
    assert fruits[0]["name"] == "banana"
    assert fruits[0].get("description") is None

    response = client.post(
        "/fruits",
        json={"name": "Apple", "description": "Keeps the doctor away"},
    )
    assert response.status_code == 201

    response = client.get("/fruits")
    assert response.status_code == 200
    fruits = response.json()
    assert any(fruit.get("description") == "Keeps the doctor away" for fruit in fruits)


def test_fruit_is_validated(client):
    response = client.post("/fruits", json={"name": "", "description": "Hola"})

    assert response.status_code == 400
