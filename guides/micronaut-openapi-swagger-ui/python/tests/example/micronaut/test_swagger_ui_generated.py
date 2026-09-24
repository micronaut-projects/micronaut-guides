import pytest

from micronaut.core.io import ResourceLoader
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(start_application=False),  # <1>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def resource_loader(my_context):
    return my_context[ResourceLoader]


def test_build_generates_swagger_ui(resource_loader):
    assert resource_loader.getResource("META-INF/swagger/views/swagger-ui/index.html").isPresent()
