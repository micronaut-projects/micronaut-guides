import pytest

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            properties={
                "datasources.default.driver-class-name": "org.testcontainers.jdbc.ContainerDatabaseDriver",
                "datasources.default.url": "jdbc:tc:mysql:8:///db",
                "kafka.enabled": "false",
            },
            transactional=False,
        ),
    )
    yield fixture
    fixture.stop()


def test_context(my_context):
    assert my_context.isRunning()
