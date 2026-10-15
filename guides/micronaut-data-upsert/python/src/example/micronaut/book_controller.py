from typing import Annotated

from micronaut.http import HttpStatus
from micronaut.http.annotation import Body, Controller, Post, Put, Status
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from .book import Book
from .book_repository import BookRepository


@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/books")  # <1>
class BookController:

    def __init__(self, book_repository: BookRepository):  # <2>
        self.book_repository = book_repository

    @Post  # <3>
    @Status(HttpStatus.CREATED)  # <4>
    def save(self, book: Annotated[Book, Body]) -> None:  # <5>
        self.book_repository.upsert(book)  # <6>

    @Put  # <7>
    @Status(HttpStatus.OK)
    def update(self, book: Annotated[Book, Body]) -> None:
        self.book_repository.upsert(book)  # <6>
