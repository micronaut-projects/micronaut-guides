import pytest
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.analytics_service import AnalyticsService
from example.micronaut.book import Book
from example.micronaut.book_analytics import BookAnalytics


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def analytics_service(my_context) -> AnalyticsService:
    return my_context["example.micronaut.AnalyticsService"]


def find_book_analytics(book: Book, analytics: list[BookAnalytics]) -> BookAnalytics:
    return next(item for item in analytics if item.book_isbn == book.isbn)


def test_update_book_analytics_and_get_analytics(analytics_service):
    b1 = Book("1491950358", "Building Microservices")
    b2 = Book("1680502395", "Release It!")

    analytics_service.update_book_analytics(b1)
    analytics_service.update_book_analytics(b1)
    analytics_service.update_book_analytics(b1)
    analytics_service.update_book_analytics(b2)

    analytics = analytics_service.list_analytics()
    assert len(analytics) == 2

    assert find_book_analytics(b1, analytics).count == 3
    assert find_book_analytics(b2, analytics).count == 1
