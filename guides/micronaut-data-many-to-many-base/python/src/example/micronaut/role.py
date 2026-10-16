from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.data.annotation import GeneratedValue, Id, MappedEntity


@dataclass
@MappedEntity  # <1>
class Role:
    authority: Annotated[str, NotBlank]  # <4>
    id: Annotated[int | None, Id, GeneratedValue] = None  # <2> <3>
