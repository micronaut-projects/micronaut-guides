from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.data.annotation import GeneratedValue, Id, MappedEntity, Version
from micronaut.serde.annotation import Serdeable


@dataclass
@Serdeable
@MappedEntity  # <1>
class Person:
    id: Annotated[int | None, Id, GeneratedValue] = None  # <2> <3>
    version: Annotated[int | None, Version] = None  # <4>
    name: Annotated[str | None, NotBlank] = None
    age: int | None = None
