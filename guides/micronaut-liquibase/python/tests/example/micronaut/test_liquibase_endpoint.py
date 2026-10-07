import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def test_migrations_are_exposed_via_an_endpoint(client):
    response = client.get("/liquibase")

    assert response.status_code == 200

    liquibase_reports = response.json()
    assert len(liquibase_reports) == 1

    liquibase_report = liquibase_reports[0]
    assert len(liquibase_report["changeSets"]) == 2
