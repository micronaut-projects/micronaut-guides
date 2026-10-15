from java.lang import Long
from dataclasses import dataclass

from java.math import BigDecimal
from micronaut.serde.annotation import Serdeable

from ..entity.booking_status import BookingStatus


@Serdeable  # <1>
@dataclass
class BookingView:
    id: Long
    seatId: str
    amount: BigDecimal
    status: BookingStatus
    paymentReference: str
