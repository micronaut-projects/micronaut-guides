from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule

from .book_inventory import BookInventory


@Produces(MediaType.TEXT_PLAIN)
@Get("/books/stock/{isbn}")
@Secured(SecurityRule.IS_AUTHENTICATED)  # <1>
def stock(isbn: Annotated[str, NotBlank]) -> bool | None:
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
