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
            properties={"endpoints.all.path": "/endpoints/"},  # <1>
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_health_endpoint_exposed_at_non_default_endpoints_path(client):
    response = client.get("/endpoints/health")  # <2>
    assert response.status_code == 200

    response = client.get("/health")
    assert response.status_code == 404  # <3>
