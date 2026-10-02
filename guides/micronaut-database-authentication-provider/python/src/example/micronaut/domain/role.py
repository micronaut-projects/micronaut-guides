from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.data.annotation import GeneratedValue, Id, MappedEntity
from micronaut.serde.annotation import Serdeable


@dataclass
@Serdeable  # <1>
@MappedEntity  # <2>
class Role:
    authority: Annotated[str, NotBlank]
    id: Annotated[int | None, Id, GeneratedValue] = None  # <3> <4>
