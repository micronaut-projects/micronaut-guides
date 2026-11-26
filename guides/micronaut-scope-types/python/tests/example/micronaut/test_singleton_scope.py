import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),  # <1>
    )
    try:
        yield fixture
    finally:
        fixture.stop()


@pytest.fixture
def client(my_context):
    with requests.with_context(my_context) as session:  # <2>
        yield session


@pytest.mark.parametrize("path", ["/singleton", "/infrastructuresingleton"])
def test_singleton_scope_instance_identity(client, path):
    response = client.get(path)
    assert response.status_code == 200
    serial_numbers = response.json()
    assert len(serial_numbers) == 2
    responses = set(serial_numbers)
    assert len(responses) == 1  # <3>

    response = client.get(path)
    assert response.status_code == 200
    responses.update(response.json())
    assert len(responses) == 1  # <4>
