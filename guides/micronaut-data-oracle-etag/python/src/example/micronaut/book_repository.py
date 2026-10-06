from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.repository import CrudRepository

from .book import Book


@JdbcRepository(dialect="ORACLE")  # <1>
class BookRepository(CrudRepository[Book, int]):
    def getById(self, id: int) -> Book | None: ...
