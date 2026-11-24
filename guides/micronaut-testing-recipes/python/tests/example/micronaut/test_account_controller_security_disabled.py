import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={"micronaut.security.filter.enabled": "false"},  # <1>
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def test_account_is_accessible_when_security_is_disabled(client):
    response = client.get("/account")
    assert response.status_code == 200  # <3>
    assert response.text == "Your account"
