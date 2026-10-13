from java.lang import Long
from dataclasses import dataclass

from java.math import BigDecimal
from micronaut.serde.annotation import Serdeable


@Serdeable
@dataclass
class PaymentRequested:
    bookingId: Long
    transactionId: str
    amount: BigDecimal
    paymentToken: str
