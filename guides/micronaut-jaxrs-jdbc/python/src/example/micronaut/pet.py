from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank, NotNull
from micronaut.data.annotation import GeneratedValue, Id, MappedEntity
from micronaut.serde.annotation import Serdeable

from .pet_type import PetType


@dataclass
@Serdeable  # <1>
@MappedEntity  # <2>
class Pet:
    name: Annotated[str, NotBlank]
    type: Annotated[PetType, NotNull] = PetType.DOG
    id: Annotated[int | None, Id, GeneratedValue] = None  # <3> <4>
