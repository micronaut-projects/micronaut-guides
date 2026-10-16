from abc import ABC, abstractmethod

from .book import Book


class BookCatalogueOperations(ABC):

    @abstractmethod
    async def find_all(self) -> list[Book]:
        ...
