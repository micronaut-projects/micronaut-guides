from java.util import Optional
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from .author import Author


@JdbcRepository(dialect=Dialect.POSTGRES)  # <1>
class AuthorRepository(CrudRepository[Author, int]):  # <2>
    def findByUsername(self, username: str) -> Optional[Author]: ...  # <3>

    def findByIdIn(self, ids: list[int]) -> list[Author]: ...  # <4>

    def findOrCreate(self, username: str) -> Author:
        existing = self.findByUsername(username).orElse(None)
        if existing is not None:
            return existing
        return self.save(Author(username))  # <5>
