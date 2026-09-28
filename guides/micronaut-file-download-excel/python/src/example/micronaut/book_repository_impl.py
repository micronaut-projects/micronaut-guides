from jakarta.inject import Singleton

from .book import Book
from .book_repository import BookRepository


@Singleton  # <1>
class BookRepositoryImpl(BookRepository):
    def find_all(self) -> list[Book]:
        return [
            Book("1491950358", "Building Microservices"),
            Book("1680502395", "Release It!"),
            Book("0321601912", "Continuous Delivery:"),
        ]
