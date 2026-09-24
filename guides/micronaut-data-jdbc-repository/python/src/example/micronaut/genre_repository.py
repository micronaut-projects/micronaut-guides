from typing import List

import java
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.repository import CrudRepository

from .domain.genre import Genre

Pageable = java.type("io.micronaut.data.model.Pageable")


@JdbcRepository(dialect="MYSQL")  # <1>
class GenreRepository(CrudRepository[Genre, int]):  # <2>
    def findAll(self, pageable: Pageable) -> List[Genre]: ...
