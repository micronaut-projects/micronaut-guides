from dataclasses import dataclass
from typing import TYPE_CHECKING, Annotated

from jakarta.validation.constraints import NotBlank
from java.time import Instant, ZoneId
from java.time.format import DateTimeFormatter
from micronaut.core.annotation import Nullable
from micronaut.data.annotation import DateCreated, GeneratedValue, Id, MappedEntity, Relation
from micronaut.serde.annotation import Serdeable

if TYPE_CHECKING:
    from .room import Room


# tag::clazz[]
@dataclass
@Serdeable
@MappedEntity  # <1>
class Message:
    content: Annotated[str, NotBlank]
    room: Annotated["Room | None", Nullable, Relation(value="MANY_TO_ONE")] = None  # <4>
    id: Annotated[int | None, Id, GeneratedValue] = None  # <2> <3>
    dateCreated: Annotated[Instant, Nullable, DateCreated] = None  # <5>

    def formattedDateCreated(self) -> str:
        formatter = DateTimeFormatter.ofPattern("MMM:dd HH:mm:ss").withZone(
            ZoneId.systemDefault()
        )
        return formatter.format(self.dateCreated)
# end::clazz[]
