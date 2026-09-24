from typing import Annotated

import java
from jakarta.inject import Inject, Singleton

from .warehouse_client import WarehouseClient

Span = java.type("io.opentelemetry.api.trace.Span")
SpanAttribute = java.type("io.opentelemetry.instrumentation.annotations.SpanAttribute")
Tracer = java.type("io.opentelemetry.api.trace.Tracer")
WithSpan = java.type("io.opentelemetry.instrumentation.annotations.WithSpan")


@Singleton
class InventoryService:
    def __init__(
        self,
        tracer: Annotated[Tracer, Inject],
        warehouse: Annotated[WarehouseClient, Inject],
    ):  # <1>
        self.tracer = tracer
        self.warehouse = warehouse
        self.inventory = {
            "laptop": 4,
            "desktop": 2,
            "monitor": 11,
        }
        self.store_name = "my_store"

    def get_product_names(self) -> list[str]:
        return list(self.inventory.keys())

    @WithSpan("stock-counts")  # <2>
    def get_stock_counts(
        self,
        item: Annotated[str, SpanAttribute("inventory.item")],
    ) -> dict[str, int]:  # <3>
        counts = {}
        if item in self.inventory:
            count = self.inventory[item]
            counts["store"] = count

            if count < 10:
                counts["warehouse"] = self._in_warehouse(self.store_name, item)

        return counts

    def _in_warehouse(self, store: str, item: str) -> int:
        Span.current().setAttribute("inventory.store-name", store)  # <4>
        return self.warehouse.get_item_count(store, self._get_upc(item))

    def order(self, item: str, count: int) -> None:
        self._order_from_warehouse(item, count)
        if item in self.inventory:
            count += self.inventory[item]
        self.inventory[item] = count

    def _order_from_warehouse(self, item: str, count: int) -> None:
        span = (
            self.tracer.spanBuilder("warehouse-order")  # <5>
            .setAttribute("item", item)
            .setAttribute("count", count)
            .startSpan()
        )

        payload = {
            "store": self.store_name,
            "product": item,
            "amount": count,
            "upc": self._get_upc(item),
        }

        self.warehouse.order(payload)
        span.end()

    def _get_upc(self, item: str) -> int:
        return abs(hash(item))
