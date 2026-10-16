from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.tracing.annotation import ContinueSpan, SpanTag

from .book_inventory import BookInventory


@Produces(MediaType.TEXT_PLAIN)
@Get("/books/stock/{isbn}")
@ContinueSpan  # <1>
def stock(isbn: Annotated[str, SpanTag("stock.isbn"), NotBlank]) -> bool | None:  # <2>
    inventory = book_inventory_by_isbn(isbn)
    if inventory is None:
        return None
    return inventory.stock > 0


def book_inventory_by_isbn(isbn: str) -> BookInventory | None:
    if isbn == "1491950358":
        return BookInventory(isbn, 4)
    if isbn == "1680502395":
        return BookInventory(isbn, 0)
    return None
