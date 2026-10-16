import java

from micronaut.http.annotation import Controller, Get
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from .book import Book
from .book_repository import BookRepository

Counted = java.type("io.micrometer.core.annotation.Counted")
Timed = java.type("io.micrometer.core.annotation.Timed")


@Controller("/books")  # <1>
@ExecuteOn(TaskExecutors.BLOCKING)  # <2>
class BookController:
    def __init__(self, book_repository: BookRepository):  # <3>
        self.book_repository = book_repository

    @Get  # <4>
    @Timed("books.index")  # <5>
    def index(self) -> list[Book]:
        return list(self.book_repository.findAll())

    @Get("/{isbn}")  # <6>
    @Counted("books.find")  # <7>
    def find_book(self, isbn: str) -> Book | None:
        return self.book_repository.findByIsbn(isbn).orElse(None)
