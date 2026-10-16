from typing import Annotated

from jakarta.validation.constraints import NotBlank
from micronaut.rabbitmq.annotation import Queue, RabbitListener

from .book_inventory import BookInventory


@RabbitListener  # <1>
class BookInventoryService:

    @Queue("inventory")  # <2>
    def stock(self, isbn: Annotated[str, NotBlank]) -> bool | None:
        inventory = self.book_inventory_by_isbn(isbn)
        return inventory.stock > 0 if inventory is not None else None

    def book_inventory_by_isbn(self, isbn: str) -> BookInventory | None:
        if isbn == "1491950358":
            return BookInventory(isbn, 4)
        if isbn == "1680502395":
            return BookInventory(isbn, 0)
        return None
