import pytest
import requests
from typing import List

from jakarta.inject import Singleton
from micronaut.context.annotation import Replaces, Requires
from org.reactivestreams import Publisher
from pyronaut.test import MicronautTest, micronaut_test_fixture
from reactor.core.publisher import Flux, Mono

from example.micronaut.default_fruit_service import DefaultFruitService
from example.micronaut.fruit import Fruit
from example.micronaut.fruit_service import FruitService


@Singleton
@Replaces(DefaultFruitService)
@Requires(property="spec.name", value="controller-isolation")
class MockFruitService(FruitService):
    def __init__(self):
        pass

    def list(self) -> Publisher[Fruit]:
        return Flux.just(
            Fruit("apple", "red"),
            Fruit("banana", "yellow"),
        )

    def save(self, fruit: Fruit) -> Publisher[Fruit]:
        return Mono.just(fruit)

    def find(self, id: str) -> Publisher[Fruit]:
        return Mono.empty()

    def findByNameInList(self, names: List[str]) -> Publisher[Fruit]:
        return Flux.empty()


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            transactional=False,  # <1>
            properties={"spec.name": "controller-isolation"},
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_controller_serialization(client):
    response = client.get("/fruits")

    assert response.status_code == 200
    assert response.headers["content-type"] == "application/json"
    all_fruits = ",".join(
        f"{fruit['name']}:{fruit['description']}"
        for fruit in response.json()
    )
    assert all_fruits == "apple:red,banana:yellow"
