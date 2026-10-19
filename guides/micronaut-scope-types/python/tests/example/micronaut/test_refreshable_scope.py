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
                "endpoints.refresh.enabled": True,
                "endpoints.refresh.sensitive": False,
            },
        ),  # <1>
    )
    try:
        yield fixture
    finally:
        fixture.stop()


@pytest.fixture
def client(my_context):
    with requests.with_context(my_context) as session:  # <2>
        yield session


def test_refreshable_scope_refreshes_bean_state(client):
    response = client.get("/refreshable")
    assert response.status_code == 200
    serial_numbers = response.json()
    assert len(serial_numbers) == 2
    responses = set(serial_numbers)
    assert len(responses) == 1  # <3>

    response = client.get("/refreshable")
    assert response.status_code == 200
    responses.update(response.json())
    assert len(responses) == 1  # <4>

    response = client.post("/refresh", json={"force": True})  # <5>
    assert response.status_code == 200

    response = client.get("/refreshable")
    assert response.status_code == 200
    serial_numbers = response.json()
    assert len(serial_numbers) == 2
    assert len(set(serial_numbers)) == 1
    responses.update(serial_numbers)
    assert len(responses) == 2  # <6>
