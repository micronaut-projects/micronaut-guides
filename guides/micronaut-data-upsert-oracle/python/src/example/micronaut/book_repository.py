from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from .book import Book


@JdbcRepository(dialect=Dialect.ORACLE)  # <1>
class BookRepository(CrudRepository[Book, int]):  # <2>

    def findByTitle(self, title: str) -> Book | None: ...

    def upsert(self, book: Book) -> None: ...  # <3>
