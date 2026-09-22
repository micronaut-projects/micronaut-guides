from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import PageableRepository

from .to_do import ToDo


@JdbcRepository(dialect=Dialect.POSTGRES)  # <1>
class ToDoRepository(PageableRepository[ToDo, int]):  # <2>
    pass
