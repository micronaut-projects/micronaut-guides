from typing import Annotated

from jakarta.inject import Inject
from micronaut.http.annotation import Get

from .book import Book
from .book_service import BookService


book_service: Annotated[BookService, Inject]  # <2>


@Get("/books")  # <3>
def list_all() -> list[Book]:
    return book_service.list_all()


@Get("/books/{isbn}")  # <4>
def find_book(isbn: str) -> Book | None:
    return book_service.find_by_isbn(isbn)
