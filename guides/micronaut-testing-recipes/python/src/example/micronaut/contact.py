from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@dataclass
class Contact:
    firstName: Annotated[str | None, NotBlank]  # <2>
    lastName: Annotated[str | None, NotBlank]
    company: str | None = None  # <3>
