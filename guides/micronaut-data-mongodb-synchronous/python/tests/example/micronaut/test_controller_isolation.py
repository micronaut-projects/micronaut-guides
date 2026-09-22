from typing import List

import pytest
import requests
from jakarta.inject import Singleton
from micronaut.context.annotation import Replaces, Requires
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.default_fruit_service import DefaultFruitService
from example.micronaut.fruit import Fruit
from example.micronaut.fruit_service import FruitService


@Singleton
@Replaces(DefaultFruitService)
@Requires(property="spec.name", value="controller-isolation")
class MockService(FruitService):
    def list(self) -> List[Fruit]:
        return [
            Fruit("apple", "red"),
            Fruit("banana", "yellow"),
        ]

    def save(self, fruit: Fruit) -> Fruit:
        return fruit

    def find(self, id: str) -> Fruit | None:
        return None

    def find_by_name_in_list(self, names: List[str]) -> List[Fruit]:
        return []


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={"spec.name": "controller-isolation"},
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_serialization(client):
    response = client.get("/fruits")

    assert response.status_code == 200
    assert response.headers["Content-Type"].startswith("application/json")

    all_fruits = ",".join(
        f"{fruit['name']}:{fruit['description']}"
        for fruit in response.json()
    )
    assert all_fruits == "apple:red,banana:yellow"
