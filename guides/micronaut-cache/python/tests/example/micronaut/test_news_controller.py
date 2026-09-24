import pytest
import requests

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)


def test_fetching_october_headlines_uses_cache(client):  # <1>
    expected = "Micronaut AOP: Awesome flexibility without the complexity"

    response = client.get("/OCTOBER")
    assert response.status_code == 200
    assert response.json()["headlines"] == [expected]

    response = client.get("/OCTOBER")
    assert response.status_code == 200
    assert response.json()["headlines"] == [expected]
