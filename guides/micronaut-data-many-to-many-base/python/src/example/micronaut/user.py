from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank, NotNull
from micronaut.core.annotation import Introspected


@dataclass
@Introspected  # <1>
class User:
    id: Annotated[int, NotNull]
    username: Annotated[str, NotBlank]
    authorities: list[str] | None = None
