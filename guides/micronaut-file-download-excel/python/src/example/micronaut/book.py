from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.core.annotation import Introspected


@Introspected
@dataclass
class Book:
    isbn: Annotated[str, NotBlank]
    name: Annotated[str, NotBlank]
