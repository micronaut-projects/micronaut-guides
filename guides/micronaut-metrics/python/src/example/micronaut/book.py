from dataclasses import dataclass
from typing import Annotated

from micronaut.data.annotation import GeneratedValue, Id, MappedEntity
from micronaut.serde.annotation import Serdeable


@dataclass
@Serdeable
@MappedEntity  # <1>
class Book:
    isbn: str
    name: str
    id: Annotated[int | None, Id, GeneratedValue] = None  # <2> <3>
