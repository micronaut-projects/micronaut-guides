from typing import List

from java.util import Optional
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.repository import CrudRepository

from ..domain.thing import Thing


@JdbcRepository(dialect="ORACLE")
class ThingRepository(CrudRepository[Thing, int]):
    def findAll(self) -> List[Thing]: ...

    def findByName(self, name: str) -> Optional[Thing]: ...
