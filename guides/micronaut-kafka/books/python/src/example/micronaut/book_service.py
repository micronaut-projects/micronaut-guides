from jakarta.inject import Singleton

from .book import Book


@Singleton
class BookService:

    def __init__(self):
        self.book_store = [
            Book("1491950358", "Building Microservices"),
            Book("1680502395", "Release It!"),
            Book("0321601912", "Continuous Delivery"),
        ]

    def list_all(self) -> list[Book]:
        return self.book_store

    def find_by_isbn(self, isbn: str) -> Book | None:
        return next((book for book in self.book_store if book.isbn == isbn), None)
