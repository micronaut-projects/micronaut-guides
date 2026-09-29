import pytest
import requests
from micronaut.data.exceptions import DataIntegrityViolationException
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.account_repository import AccountRepository
from example.micronaut.domain.account import Account


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def repository(my_context):
    return my_context[AccountRepository]


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_reservation_updates_multiple_fields(repository):
    account = repository.save(Account(None, "Checking", 100, 50))

    updated = repository.reserveIncrementBalanceAndDecrementCredit(account.id, 25, 10)  # <2>

    assert updated == 1
    found = repository.findById(account.id).orElseThrow()
    assert found.balance == 125
    assert found.credit == 40


def test_reservation_constraint_failure_is_mapped(repository):
    account = repository.save(Account(None, "Checking", 100, 50))

    with pytest.raises(BaseException) as error:
        repository.reserveIncrementBalanceAndDecrementCredit(account.id, 0, 1000)  # <3>
    assert isinstance(error.value, DataIntegrityViolationException)

    found = repository.findById(account.id).orElseThrow()
    assert found.credit == 50  # <4>


def test_reservation_constraint_failure_responds_with_conflict(repository, client):
    account = repository.save(Account(None, "Checking", 100, 50))

    response = client.post(f"/accounts/{account.id}/reserve?balance=0&credit=1000")

    assert response.status_code == 409  # <5>
    assert "The operation violates an account constraint" in response.text  # <6>


def test_account_is_created_and_reserved_over_http(client):
    created = client.post("/accounts", json={"name": "Savings", "balance": 100, "credit": 50})  # <7>
    assert created.status_code == 201
    account = created.json()

    reserved = client.post(f"/accounts/{account['id']}/reserve?balance=25&credit=10").json()  # <8>
    assert reserved["balance"] == 125
    assert reserved["credit"] == 40


def test_reservation_for_unknown_account_responds_with_not_found(client):
    response = client.post("/accounts/-1/reserve?balance=1&credit=1")

    assert response.status_code == 404  # <9>
