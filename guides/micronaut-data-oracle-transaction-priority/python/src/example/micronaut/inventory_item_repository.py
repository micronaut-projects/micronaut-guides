from java.util import Optional
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from .inventory_item import InventoryItem


@JdbcRepository(dialect=Dialect.ORACLE)  # <1>
class InventoryItemRepository(CrudRepository[InventoryItem, int]):

    def findByIdForUpdate(self, id: int) -> Optional[InventoryItem]: ...  # <2>
