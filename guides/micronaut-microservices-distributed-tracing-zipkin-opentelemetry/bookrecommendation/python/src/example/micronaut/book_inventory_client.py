from abc import abstractmethod
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.core.async_.annotation import SingleResult
from micronaut.http import MediaType
from micronaut.http.annotation import Consumes, Get
from micronaut.http.client.annotation import Client
from micronaut.retry.annotation import Recoverable
from org.reactivestreams import Publisher

from .book_inventory_operations import BookInventoryOperations


@Client("http://localhost:8082")
@Recoverable(api=BookInventoryOperations)
class BookInventoryClient(BookInventoryOperations):

    @Consumes(MediaType.TEXT_PLAIN)
    @Get("/books/stock/{isbn}")
    @SingleResult
    @abstractmethod
    def stock(self, isbn: Annotated[str, NotBlank]) -> Publisher[bool]:
        ...
