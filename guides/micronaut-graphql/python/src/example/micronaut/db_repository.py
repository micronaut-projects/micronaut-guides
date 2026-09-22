from jakarta.inject import Singleton

from .author import Author
from .book import Book


@Singleton
class DbRepository:
    def __init__(self):
        self._books = [  # <1>
            Book(
                "book-1",
                "Harry Potter and the Philosopher's Stone",
                223,
                Author("author-1", "Joanne", "Rowling"),
            ),
            Book("book-2", "Moby Dick", 635, Author("author-2", "Herman", "Melville")),
            Book(
                "book-3",
                "Interview with the vampire",
                371,
                Author("author-3", "Anne", "Rice"),
            ),
        ]

    def find_all_books(self) -> list[Book]:
        return self._books

    def find_all_authors(self) -> list[Author]:
        return [book.author for book in self._books]
