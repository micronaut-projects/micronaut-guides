from abc import abstractmethod

from micronaut.http.annotation import Get
from micronaut.http.client.annotation import Client
from micronaut.retry.annotation import Recoverable

from .book import Book
from .book_catalogue_operations import BookCatalogueOperations


@Client("http://localhost:8081")  # <1>
@Recoverable(api=BookCatalogueOperations)
class BookCatalogueClient(BookCatalogueOperations):

    @Get("/books")
    @abstractmethod
    async def find_all(self) -> list[Book]:
        ...
