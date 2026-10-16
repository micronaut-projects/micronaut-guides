from typing import Annotated

from jakarta.validation.constraints import NotBlank, NotNull
from java.util import Optional
from micronaut.core.annotation import NonNull
from micronaut.data.annotation import Id, Join
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from ..entities.room import Room


# tag::clazz[]
@JdbcRepository(dialect=Dialect.MYSQL)  # <1>
class RoomRepository(CrudRepository[Room, int]):  # <2>
    def save(self, name: Annotated[str, NonNull, NotBlank]) -> Room: ...

    def update(
        self,
        id: Annotated[int, Id],
        name: Annotated[str, NonNull, NotBlank],
    ) -> None: ...

    @Join(value="messages", type=Join.Type.LEFT_FETCH)  # <3>
    def getById(self, id: Annotated[int, NonNull, NotNull]) -> Optional[Room]: ...
# end::clazz[]
