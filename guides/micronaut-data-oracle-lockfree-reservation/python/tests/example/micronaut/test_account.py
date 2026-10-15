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


def test_deposit_increments_balance(repository):
    account = repository.save(Account(None, "Checking", 100))

    updated = repository.reserveIncrementBalance(account.id, 25)  # <2>

    assert updated == 1
    found = repository.findById(account.id).orElseThrow()
    assert found.balance == 125


def test_withdrawal_beyond_balance_is_rejected(repository):
    account = repository.save(Account(None, "Checking", 100))

    with pytest.raises(BaseException) as error:
        repository.reserveDecrementBalance(account.id, 1000)  # <3>
    assert isinstance(error.value, DataIntegrityViolationException)

    found = repository.findById(account.id).orElseThrow()
    assert found.balance == 100  # <4>


def test_withdrawal_beyond_balance_responds_with_conflict(repository, client):
    account = repository.save(Account(None, "Checking", 100))

    response = client.post(f"/accounts/{account.id}/withdraw?amount=1000")

    assert response.status_code == 409  # <5>
    assert "The operation violates an account constraint" in response.text  # <6>


def test_account_is_created_deposited_and_withdrawn_over_http(client):
    created = client.post("/accounts", json={"name": "Savings", "balance": 100})  # <7>
    assert created.status_code == 201
    account = created.json()

    deposited = client.post(f"/accounts/{account['id']}/deposit?amount=25").json()  # <8>
    assert deposited["balance"] == 125

    withdrawn = client.post(f"/accounts/{account['id']}/withdraw?amount=50").json()
    assert withdrawn["balance"] == 75


def test_deposit_for_unknown_account_responds_with_not_found(client):
    response = client.post("/accounts/-1/deposit?amount=1")

    assert response.status_code == 404  # <9>
