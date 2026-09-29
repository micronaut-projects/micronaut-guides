import time

import pytest
import requests
from java.sql import SQLException
from java.util.concurrent import CompletableFuture, TimeUnit
from micronaut.transaction import TransactionOperations
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.inventory_service import DEMO_ITEM_ID

ORA_RESOURCE_BUSY = 54


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    client = requests.with_context(my_context)
    client.post("/inventory/reset")
    return client


def test_reconciliation_commits_without_contention(client):
    response = client.post("/inventory/reconcile?countSeconds=1")

    assert response.status_code == 200
    assert response.json()["status"] == "RECONCILED"
    assert response.json()["available_quantity"] == 1


def test_high_priority_checkout_rolls_back_low_priority_reconciliation(client, my_context):
    reconciliation = CompletableFuture.supplyAsync(
        lambda: client.post("/inventory/reconcile?countSeconds=8"))  # <2>
    await_item_locked(my_context[TransactionOperations])  # <3>

    checked_out = client.post("/inventory/checkout")  # <4>
    assert checked_out.status_code == 200
    assert checked_out.json()["status"] == "CHECKED_OUT"

    rolled_back = reconciliation.get(15, TimeUnit.SECONDS)
    assert rolled_back.status_code == 409  # <5>
    assert "Oracle rolled back this operation in favor of a higher-priority transaction" in rolled_back.text  # <6>

    item = client.get("/inventory").json()
    assert item["status"] == "CHECKED_OUT"  # <7>
    assert item["available_quantity"] == 0


def test_reconciliation_rejects_invalid_count_duration(client):
    response = client.post("/inventory/reconcile?countSeconds=0")

    assert response.status_code == 400  # <8>


def await_item_locked(transaction_operations):
    deadline = time.monotonic() + 10
    while time.monotonic() < deadline:
        if is_item_locked(transaction_operations):
            return
        time.sleep(0.1)
    pytest.fail("The reconciliation did not lock the inventory item")


def is_item_locked(transaction_operations) -> bool:
    def probe(status) -> bool:
        statement = status.getConnection().prepareStatement(
            "SELECT id FROM inventory_item WHERE id = ? FOR UPDATE NOWAIT")
        try:
            statement.setLong(1, DEMO_ITEM_ID)
            statement.executeQuery().close()
            return False
        except SQLException as e:
            if e.getErrorCode() == ORA_RESOURCE_BUSY:
                return True
            raise
        finally:
            statement.close()

    return transaction_operations.executeWrite(probe)
