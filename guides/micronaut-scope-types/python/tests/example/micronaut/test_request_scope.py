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
        MicronautTest(environments=["test"], transactional=False),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>
# end::imports[]


# tag::test[]
def test_request_scope_associates_the_instance_with_each_http_request(client):
    path = "/request"
    responses = set(execute_request(client, path))
    assert len(responses) == 1  # <3>
    responses.update(execute_request(client, path))
    assert len(responses) == 2  # <4>


def execute_request(client, path: str) -> list[str]:
    response = client.get(path)
    assert response.status_code == 200
    return json.loads(response.text)
# end::test[]
