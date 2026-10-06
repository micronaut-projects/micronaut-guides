from abc import abstractmethod

from micronaut.http.annotation import Get
from micronaut.http.client.annotation import Client
from micronaut.retry.annotation import Recoverable
from org.reactivestreams import Publisher

from .book import Book
from .book_catalogue_operations import BookCatalogueOperations


@Client(id="bookcatalogue")  # <1>
@Recoverable(api=BookCatalogueOperations)
class BookCatalogueClient(BookCatalogueOperations):

    @Get("/books")
    @abstractmethod
    def findAll(self) -> Publisher[Book]:
        ...
