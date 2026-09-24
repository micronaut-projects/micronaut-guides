import pytest
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.book_catalogue_service import BookCatalogueService


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def book_catalogue_service(my_context) -> BookCatalogueService:
    return my_context["example.micronaut.BookCatalogueService"]


def test_list_books(book_catalogue_service):
    books = book_catalogue_service.list_books()

    assert len(books) == 3
    assert books[0].isbn == "1491950358"
    assert books[0].name == "Building Microservices"
