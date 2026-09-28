from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotNull
from micronaut.core.annotation import Introspected
from micronaut.data.annotation import GeneratedValue, Id, MappedEntity


@dataclass
@Introspected
@MappedEntity  # <1>
class ToDo:
    title: Annotated[str, NotNull]
    authorId: int
    completed: bool = False
    id: Annotated[int | None, Id, GeneratedValue] = None  # <2>
