from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable


@Serdeable
@dataclass(frozen=True)
class BookInventory:
    isbn: str
    stock: int
