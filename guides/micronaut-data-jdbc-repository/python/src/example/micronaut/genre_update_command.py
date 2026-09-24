from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank, NotNull
from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@dataclass
class GenreUpdateCommand:
    id: Annotated[int, NotNull]
    name: Annotated[str, NotBlank]
