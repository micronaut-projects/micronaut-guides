from dataclasses import dataclass
from typing import Annotated

from micronaut.data.annotation import Id, MappedEntity
from micronaut.serde.annotation import Serdeable

from .status import Status


@Serdeable
@MappedEntity("inventory_item")
@dataclass
class InventoryItem:
    id: Annotated[int, Id]
    name: str
    available_quantity: int
    status: Status
