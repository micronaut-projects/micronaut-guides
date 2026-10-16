from abc import ABC, abstractmethod

from org.reactivestreams import Publisher

from .book import Book


class BookCatalogueOperations(ABC):

    @abstractmethod
    def findAll(self) -> Publisher[Book]:
        ...
