from dataclasses import dataclass
from decimal import Decimal

from java.lang import Long
from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@dataclass
class SalaryAggregates:
    min: Decimal | None
    max: Decimal | None
    sum: Decimal | None
    count: Long | None
