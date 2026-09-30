from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.data.annotation import GeneratedValue, Id, MappedEntity
from micronaut.serde.annotation import Serdeable


@dataclass
@Serdeable
@MappedEntity  # <1>
class Genre:
    id: Annotated[int | None, Id, GeneratedValue] = None
    name: Annotated[str | None, NotBlank] = None
