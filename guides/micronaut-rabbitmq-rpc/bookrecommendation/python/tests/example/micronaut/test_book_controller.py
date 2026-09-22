from time import monotonic, sleep

import pytest
import requests
from micronaut.rabbitmq.annotation import Queue, RabbitListener
from pyronaut.test import MicronautTest, micronaut_test_fixture
from reactor.core.publisher import Flux

from example.micronaut.book import Book
from example.micronaut.catalogue_client import CatalogueClient
from example.micronaut.inventory_client import InventoryClient


@RabbitListener
class CatalogueTestListener:

    @Queue("catalogue")
    def list_books(self) -> list[Book]:
        return [
            Book("1491950358", "Building Microservices"),
            Book("1680502395", "Release It!"),
        ]


@RabbitListener
class InventoryTestListener:

    @Queue("inventory")
    def stock(self, isbn: str) -> bool | None:
        if isbn == "1491950358":
            return True
        if isbn == "1680502395":
            return False
        return None


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


@pytest.fixture
def catalogue_client(my_context):
    return my_context[CatalogueClient]


@pytest.fixture
def inventory_client(my_context):
    return my_context[InventoryClient]


def wait_for_rabbitmq_rpc(catalogue_client: CatalogueClient, inventory_client: InventoryClient):
    deadline = monotonic() + 10.0
    last_error = None
    while monotonic() < deadline:
        try:
            books = Flux.from_(catalogue_client.find_all(b"")).blockFirst()
            in_stock = Flux.from_(inventory_client.stock("1491950358")).blockFirst()
            if books and in_stock is True:
                return
        except BaseException as e:
            last_error = e
        sleep(0.25)
    raise AssertionError("RabbitMQ RPC did not become ready") from last_error


def test_recommendations_are_returned_from_rabbitmq_rpc(client, catalogue_client, inventory_client):
    wait_for_rabbitmq_rpc(catalogue_client, inventory_client)

    response = client.get("/books")

    assert response.status_code == 200, response.text
    assert response.json() == [{"name": "Building Microservices"}]
