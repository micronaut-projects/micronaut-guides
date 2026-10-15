from dataclasses import dataclass
from typing import Annotated

from micronaut.data.annotation import Id, MappedEntity
from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@MappedEntity  # <2>
@dataclass
class Book:
    id: Annotated[int, Id]  # <3>
    title: str
    pages: int | None = None
