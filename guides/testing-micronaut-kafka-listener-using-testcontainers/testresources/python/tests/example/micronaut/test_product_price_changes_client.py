from abc import ABC, abstractmethod
from typing import Annotated

from micronaut.configuration.kafka.annotation import KafkaClient, KafkaKey, Topic

from example.micronaut.product_price_changed_event import ProductPriceChangedEvent


@KafkaClient
class ProductPriceChangesClient(ABC):

    @Topic("product-price-changes")
    @abstractmethod
    def send(
        self,
        product_code: Annotated[str, KafkaKey],
        event: ProductPriceChangedEvent,
    ) -> None:
        ...
