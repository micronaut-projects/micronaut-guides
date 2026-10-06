from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.repository import CrudRepository

from ..domain.thing import Thing


@JdbcRepository(dialect="ORACLE")
class ThingRepository(CrudRepository[Thing, int]):
    def findAll(self) -> list[Thing]: ...

    def findByName(self, name: str) -> Thing | None: ...
