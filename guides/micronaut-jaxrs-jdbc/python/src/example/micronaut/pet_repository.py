from java.util import Optional
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.repository import CrudRepository

from .name_dto import NameDto
from .pet import Pet
from .pet_type import PetType


@JdbcRepository(dialect="MYSQL")  # <1>
class PetRepository(CrudRepository[Pet, int]):  # <2>
    def list(self) -> list[NameDto]: ...  # <3>

    def findByName(self, name: str) -> Optional[Pet]: ...

    def save(self, name: str, type: PetType) -> Pet: ...
