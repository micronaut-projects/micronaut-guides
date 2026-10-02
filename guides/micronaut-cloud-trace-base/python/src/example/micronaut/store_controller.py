from typing import Annotated

from micronaut.http import HttpStatus
from micronaut.http.annotation import Body, Controller, Get, Post, Status
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn
from micronaut.tracing.annotation import ContinueSpan, NewSpan, SpanTag

from .inventory_service import InventoryService


@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/store")
class StoreController:
    def __init__(self, inventory: InventoryService):
        self.inventory = inventory

    @Post("/order")
    @Status(HttpStatus.CREATED)
    @NewSpan("store.order")  # <1>
    def order(
        self,
        item: Annotated[str, SpanTag("order.item"), Body("item")],
        count: Annotated[int, SpanTag, Body("count")],
    ) -> None:  # <2>
        self.inventory.order(item, count)

    @Get("/inventory")  # <3>
    def get_inventory(self) -> list[dict]:
        return [
            self.get_inventory_item(product)
            for product in self.inventory.get_product_names()
        ]

    @Get("/inventory/{item}")
    @ContinueSpan  # <4>
    def get_inventory_item(
        self,
        item: Annotated[str, SpanTag("item")],
    ) -> dict:  # <5>
        counts = dict(self.inventory.get_stock_counts(item))
        if not counts:
            counts["note"] = "Not available at store"

        counts["item"] = item
        return counts
