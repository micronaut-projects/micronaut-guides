from abc import ABC, abstractmethod
from typing import Annotated

import java
from micronaut.http.annotation import Get, Post, QueryValue
from micronaut.http.client.annotation import Client
from micronaut.tracing.annotation import ContinueSpan, SpanTag

SpanAttribute = java.type("io.opentelemetry.instrumentation.annotations.SpanAttribute")
WithSpan = java.type("io.opentelemetry.instrumentation.annotations.WithSpan")


@Client("/warehouse")  # <1>
class WarehouseClient(ABC):

    @Post("/order")
    @WithSpan
    @abstractmethod
    def order(
        self,
        payload: Annotated[dict, SpanTag("warehouse.order")],
    ) -> None:
        ...

    @Get("/count")
    @ContinueSpan
    @abstractmethod
    def get_item_count(
        self,
        store: Annotated[str, QueryValue],
        upc: Annotated[int, SpanAttribute, QueryValue],
    ) -> int:
        ...
