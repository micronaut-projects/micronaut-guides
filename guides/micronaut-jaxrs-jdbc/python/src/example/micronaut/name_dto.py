from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.serde.annotation import Serdeable


@dataclass
@Serdeable  # <1>
class NameDto:
    name: Annotated[str, NotBlank]
