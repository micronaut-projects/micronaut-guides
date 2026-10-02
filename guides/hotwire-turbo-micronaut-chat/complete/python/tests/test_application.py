import pytest

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            transactional=False,
            properties={"micronaut.http.client.follow-redirects": "false"},
        ),
    )
    yield fixture
    fixture.stop()


def test_context(my_context):
    assert my_context.isRunning()
