from typing import Annotated

from jakarta.validation.constraints import NotBlank
from java.util import Optional
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from .book import Book


@JdbcRepository(dialect=Dialect.H2)  # <1>
class BookRepository(CrudRepository[Book, int]):  # <2>
    def findByIsbn(self, isbn: Annotated[str, NotBlank]) -> Optional[Book]: ...
