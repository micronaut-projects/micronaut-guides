from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank, NotNull
from micronaut.serde.annotation import Serdeable

from .pet_type import PetType


@dataclass
@Serdeable
class PetSave:
    name: Annotated[str, NotBlank]
    type: Annotated[PetType, NotNull]
