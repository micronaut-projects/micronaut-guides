import pytest

from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(environments=["test"], transactional=False),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def my_tools(my_context):
    return my_context["example.micronaut.MyTools"]


def test_free_disk_space_tool(my_tools):
    assert "Free disk space" in my_tools.freeDiskSpace()


def test_context(my_context):
    assert my_context.isRunning()
