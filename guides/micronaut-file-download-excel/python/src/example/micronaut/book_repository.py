from typing import Protocol

from .book import Book


class BookRepository(Protocol):
    def find_all(self) -> list[Book]:
        ...
