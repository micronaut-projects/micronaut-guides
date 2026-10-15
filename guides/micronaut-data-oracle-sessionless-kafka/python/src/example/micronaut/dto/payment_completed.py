from java.lang import Long
from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
@dataclass
class PaymentCompleted:
    bookingId: Long
    transactionId: str
    approved: bool
    paymentReference: str
