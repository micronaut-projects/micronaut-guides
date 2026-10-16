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


def test_home_controller_is_hidden(resource_loader):
    adoc_input_stream = resource_loader.getResourceAsStream(
        "META-INF/swagger/micronaut-guides-1.0.adoc"
    )

    assert adoc_input_stream.isPresent()
    adoc = bytes(adoc_input_stream.get().readAllBytes()).decode()
    assert "=== __GET__ `/`" not in adoc
