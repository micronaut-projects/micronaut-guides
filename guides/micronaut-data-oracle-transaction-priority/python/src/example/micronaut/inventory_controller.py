from typing import Annotated

from jakarta.validation.constraints import Max, Min
from micronaut.http.annotation import Controller, Get, Post, QueryValue
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from .inventory_item import InventoryItem
from .inventory_service import InventoryService


@ExecuteOn(TaskExecutors.BLOCKING)  # <1>
@Controller("/inventory")
class InventoryController:

    def __init__(self, inventory_service: InventoryService):
        self.inventory_service = inventory_service

    @Get
    def item(self) -> InventoryItem:
        return self.inventory_service.find()

    @Post("/reset")
    def reset(self) -> InventoryItem:
        return self.inventory_service.reset()

    @Post("/reconcile")
    def reconcile(self, count_seconds: Annotated[
            int, QueryValue(value="countSeconds", defaultValue="20"), Min(1), Max(120)]) -> InventoryItem:  # <2>
        return self.inventory_service.reconcile(count_seconds)

    @Post("/checkout")
    def checkout(self) -> InventoryItem:
        return self.inventory_service.checkout()
