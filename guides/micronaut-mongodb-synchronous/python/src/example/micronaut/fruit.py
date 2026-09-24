from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@dataclass
class Fruit:
    name: Annotated[str, NotBlank]  # <2>
    description: str | None = None
