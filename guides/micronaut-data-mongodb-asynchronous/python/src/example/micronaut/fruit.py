from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.data.annotation import GeneratedValue, Id, MappedEntity
from micronaut.serde.annotation import Serdeable


@Serdeable
@MappedEntity  # <1>
@dataclass
class Fruit:
    name: Annotated[str, NotBlank]  # <3>
    description: str | None = None
    id: Annotated[str | None, Id, GeneratedValue] = None  # <2>
