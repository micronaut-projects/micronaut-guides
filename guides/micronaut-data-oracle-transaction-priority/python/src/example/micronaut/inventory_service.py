import time

from jakarta.inject import Singleton
from micronaut.transaction.annotation import OracleTransactional, Transactional

from .inventory_item import InventoryItem
from .inventory_item_repository import InventoryItemRepository
from .status import Status

DEMO_ITEM_ID = 1


@Singleton
class InventoryService:

    def __init__(self, inventory_item_repository: InventoryItemRepository):
        self.inventory_item_repository = inventory_item_repository

    @Transactional
    def reset(self) -> InventoryItem:
        item = InventoryItem(DEMO_ITEM_ID, "Last available item", 1, Status.AVAILABLE)
        if self.inventory_item_repository.existsById(DEMO_ITEM_ID):
            return self.inventory_item_repository.update(item)
        return self.inventory_item_repository.save(item)

    @Transactional(readOnly=True)
    def find(self) -> InventoryItem:
        return self.inventory_item_repository.findById(DEMO_ITEM_ID).orElseThrow()

    @OracleTransactional(priority=OracleTransactional.Priority.LOW)  # <1>
    def reconcile(self, count_seconds: int) -> InventoryItem:
        item = self.inventory_item_repository.findByIdForUpdate(DEMO_ITEM_ID).orElseThrow()  # <2>
        time.sleep(count_seconds)  # <3>
        return self.inventory_item_repository.update(
            InventoryItem(item.id, item.name, item.available_quantity, Status.RECONCILED))  # <4>

    @OracleTransactional(priority=OracleTransactional.Priority.HIGH)  # <5>
    def checkout(self) -> InventoryItem:
        item = self.inventory_item_repository.findByIdForUpdate(DEMO_ITEM_ID).orElseThrow()  # <6>
        return self.inventory_item_repository.update(
            InventoryItem(item.id, item.name, 0, Status.CHECKED_OUT))
