import logging

from jakarta.inject import Singleton
from jakarta.transaction import Transactional
from micronaut.configuration.kafka.annotation import KafkaListener, OffsetReset, Topic

from .product_price_changed_event import ProductPriceChangedEvent
from .product_repository import ProductRepository


@Singleton  # <1>
@Transactional  # <2>
@KafkaListener(offsetReset=OffsetReset.EARLIEST, groupId="demo")  # <5>
class ProductPriceChangedEventHandler:
    def __init__(self, product_repository: ProductRepository):  # <3>
        self.product_repository = product_repository
        self.log = logging.getLogger(__name__)

    @Topic("product-price-changes")  # <4>
    def handle(self, event: ProductPriceChangedEvent) -> None:
        self.log.info("Received a ProductPriceChangedEvent with productCode: %s", event.productCode)
        self.product_repository.updateByCode(event.productCode, event.price)
