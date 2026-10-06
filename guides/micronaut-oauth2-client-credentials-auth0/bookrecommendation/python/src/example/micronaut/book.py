from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.serde.annotation import Serdeable


@Serdeable
@dataclass(frozen=True)
class Book:
    isbn: Annotated[str, NotBlank]
    name: Annotated[str, NotBlank]
