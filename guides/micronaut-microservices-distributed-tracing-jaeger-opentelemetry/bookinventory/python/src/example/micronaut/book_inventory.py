from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotBlank, NotNull
from micronaut.core.annotation import Introspected


@Introspected
@dataclass(frozen=True)
class BookInventory:
    isbn: Annotated[str, NotBlank]
    stock: Annotated[int, NotNull]
