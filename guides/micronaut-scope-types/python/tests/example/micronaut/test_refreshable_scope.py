# tag::package[]
# end::package[]
# tag::imports[]
import json

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
            properties={
                "endpoints.refresh.enabled": "true",  # <1>
                "endpoints.refresh.sensitive": "false",  # <2>
            },
        ),  # <3>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <4>
# end::imports[]


# tag::test[]
def test_refreshable_scope_refreshes_bean_state_via_the_refresh_endpoint(client):
    path = "/refreshable"
    responses = set(execute_request(client, path))
    assert len(responses) == 1  # <5>
    responses.update(execute_request(client, path))
    assert len(responses) == 1  # <6>
    refresh(client)  # <7>
    responses.update(execute_request(client, path))
    assert len(responses) == 2  # <8>


def refresh(client):
    response = client.post("/refresh", json={"force": True})
    assert response.status_code == 200


def execute_request(client, path: str) -> list[str]:
    response = client.get(path)
    assert response.status_code == 200
    return json.loads(response.text)
# end::test[]
