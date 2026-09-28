from jakarta.inject import Singleton

from .book import Book
from .book_analytics import BookAnalytics


@Singleton
class AnalyticsService:

    def __init__(self):
        self.book_analytics: dict[Book, int] = {}  # <1>

    def update_book_analytics(self, book: Book) -> None:  # <2>
        self.book_analytics[book] = self.book_analytics.get(book, 0) + 1

    def list_analytics(self) -> list[BookAnalytics]:  # <3>
        return [
            BookAnalytics(book.isbn, count)
            for book, count in self.book_analytics.items()
        ]
