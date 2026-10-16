from dataclasses import dataclass, field
from typing import Annotated

from jakarta.validation.constraints import NotNull
from micronaut.core.annotation import Nullable
from micronaut.data.annotation import GeneratedValue, Id, MappedEntity, Relation
from micronaut.serde.annotation import Serdeable

from .message import Message


# tag::clazz[]
@dataclass
@Serdeable
@MappedEntity  # <1>
class Room:
    name: Annotated[str, NotNull]
    messages: Annotated[
        list[Message] | None,
        Nullable,
        Relation(value="ONE_TO_MANY", mappedBy="room"),
    ] = field(default_factory=list)  # <4>
    id: Annotated[int | None, Id, GeneratedValue] = None  # <2> <3>
# end::clazz[]
