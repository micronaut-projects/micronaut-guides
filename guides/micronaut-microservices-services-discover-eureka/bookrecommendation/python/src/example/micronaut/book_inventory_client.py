from abc import abstractmethod
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.http import MediaType
from micronaut.http.annotation import Consumes, Get
from micronaut.http.client.annotation import Client
from micronaut.retry.annotation import Recoverable

from .book_inventory_operations import BookInventoryOperations


@Client(id="bookinventory")  # <1>
@Recoverable(api=BookInventoryOperations)
class BookInventoryClient(BookInventoryOperations):

    @Consumes(MediaType.TEXT_PLAIN)
    @Get("/books/stock/{isbn}")
    @abstractmethod
    async def stock(self, isbn: Annotated[str, NotBlank]) -> bool | None:
        ...
