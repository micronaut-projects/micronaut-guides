import pytest
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.book_inventory_service import BookInventoryService


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),
    )
    yield fixture
    fixture.stop()


@pytest.fixture
def book_inventory_service(my_context) -> BookInventoryService:
    return my_context["example.micronaut.BookInventoryService"]


def test_stock(book_inventory_service):
    assert book_inventory_service.stock("1491950358") is True
    assert book_inventory_service.stock("1680502395") is False
    assert book_inventory_service.stock("INVALID") is None
