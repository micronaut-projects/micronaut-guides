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
                "endpoints.health.disk-space.threshold": "999999999999999999",  # <1>
            },
        ),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_health_endpoint_exposes_out_of_disk_space(client):
    response = client.get("/health")

    assert response.status_code == 503  # <2>
    assert "DOWN" in response.text  # <3>
