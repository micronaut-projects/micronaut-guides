import pytest

from micronaut.http import HttpRequest
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.color_fetcher import ColorFetcher


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(start_application=False, transactional=False),  # <1>
    )
    yield fixture
    fixture.stop()


def test_composite_color_fetcher_is_the_primary_bean_of_type_color_fetcher(my_context):
    color_fetcher = my_context[ColorFetcher]

    assert color_fetcher.favourite_color(
        HttpRequest.GET("/color").header("color", "yellow")
    ) == "yellow"
    assert color_fetcher.favourite_color(HttpRequest.GET("/color/mint")) == "mint"
