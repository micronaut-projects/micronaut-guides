import hashlib
from pathlib import Path

import pytest
import requests
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),
    )  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <2>


def test_download_file(client):
    expected = hashlib.sha256(Path("tests-config/micronaut5K.png").read_bytes()).hexdigest()

    response = client.get("/", timeout=30)  # <3>

    assert response.status_code == 200
    assert hashlib.sha256(response.content).hexdigest() == expected
