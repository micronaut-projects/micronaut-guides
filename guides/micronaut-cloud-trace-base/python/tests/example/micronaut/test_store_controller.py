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
def client(my_context):
    return requests.with_context(my_context)


def test_inventory_item(client):
    response = client.get("/store/inventory/laptop")

    assert response.status_code == 200
    inventory = response.json()
    assert inventory["item"] == "laptop"
    assert inventory["store"] == 4
    assert "warehouse" in inventory


def test_inventory_item_not_found(client):
    response = client.get("/store/inventory/chair")

    assert response.status_code == 200
    inventory = response.json()
    assert inventory["item"] == "chair"
    assert inventory["note"] == "Not available at store"


def test_inventory_all(client):
    response = client.get("/store/inventory")

    assert response.status_code == 200
    inventory = response.json()
    assert len(inventory) == 3
    names = {item["item"] for item in inventory}
    assert names == {"desktop", "monitor", "laptop"}


def test_order(client):
    response = client.post("/store/order", json={"item": "desktop", "count": 8})

    assert response.status_code == 201

    response = client.get("/store/inventory/desktop")
    assert response.status_code == 200
    inventory = response.json()
    assert inventory["item"] == "desktop"
    assert inventory["store"] == 10
    assert "warehouse" not in inventory
