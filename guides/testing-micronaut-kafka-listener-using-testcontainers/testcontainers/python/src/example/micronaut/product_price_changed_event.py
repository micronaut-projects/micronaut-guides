from dataclasses import dataclass

from java.math import BigDecimal
from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@dataclass(frozen=True)
class ProductPriceChangedEvent:
    productCode: str
    price: BigDecimal
