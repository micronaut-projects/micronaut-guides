from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model import Pageable
from micronaut.data.repository import CrudRepository

from .domain.genre import Genre

@JdbcRepository(dialect="MYSQL")  # <1>
class GenreRepository(CrudRepository[Genre, int]):  # <2>
    def findAll(self, pageable: Pageable) -> list[Genre]: ...
