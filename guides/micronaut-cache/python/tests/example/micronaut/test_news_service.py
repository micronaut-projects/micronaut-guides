from time import monotonic

import pytest
from java.time import Month
from pyronaut.test import MicronautTest, micronaut_test_fixture


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),  # <2>
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def news_service(my_context):
    return my_context["example.micronaut.NewsService"]  # <3>


def assert_duration_under(seconds: float, operation):
    start = monotonic()
    result = operation()
    duration = monotonic() - start
    assert duration < seconds
    return result


def test_cacheable_method_uses_cache(news_service):
    headlines = news_service.headlines(Month.NOVEMBER)
    assert len(headlines) == 2

    headlines = assert_duration_under(1, lambda: news_service.headlines(Month.NOVEMBER))
    assert len(headlines) == 2


def test_cache_put_updates_cache(news_service):
    news_service.headlines(Month.NOVEMBER)

    headlines = news_service.add_headline(
        Month.NOVEMBER,
        "Micronaut 1.3 Milestone 1 Released",
    )
    assert len(headlines) == 3

    headlines = assert_duration_under(1, lambda: news_service.headlines(Month.NOVEMBER))
    assert len(headlines) == 3


def test_cache_invalidate_removes_cached_value(news_service):
    news_service.headlines(Month.NOVEMBER)
    news_service.remove_headline(Month.NOVEMBER, "Micronaut AOP: Awesome flexibility without the complexity")

    headlines = news_service.headlines(Month.NOVEMBER)
    assert len(headlines) == 1
