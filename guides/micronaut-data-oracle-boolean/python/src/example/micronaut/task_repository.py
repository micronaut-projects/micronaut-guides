from typing import Annotated

from micronaut.data.annotation import Id
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from .task import Task


@JdbcRepository(dialect=Dialect.ORACLE, version="23.1")  # <1>
class TaskRepository(CrudRepository[Task, int]):

    def findByCompletedTrue(self) -> list[Task]: ...  # <2>

    def findByCompletedFalse(self) -> list[Task]: ...  # <3>

    def findByCompleted(self, completed: bool) -> list[Task]: ...  # <4>

    def updateCompleted(self, id: Annotated[int, Id], completed: bool) -> int: ...  # <5>
