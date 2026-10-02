from abc import ABC, abstractmethod
from typing import Annotated

from micronaut.configuration.kafka.annotation import KafkaClient, KafkaKey, Topic

from example.micronaut.product_price_changed_event import ProductPriceChangedEvent


@KafkaClient  # <1>
class ProductPriceChangesClient(ABC):

    @Topic("product-price-changes")  # <2>
    @abstractmethod
    def send(
        self,
        product_code: Annotated[str, KafkaKey],  # <3>
        event: ProductPriceChangedEvent,
    ) -> None:
        ...
