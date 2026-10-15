from java.lang import Long
from dataclasses import dataclass
from typing import Annotated

from java.math import BigDecimal
from micronaut.data.annotation import GeneratedValue, Id, MappedEntity

from .booking_status import BookingStatus


@MappedEntity("booking")  # <1>
@dataclass
class Booking:
    seatId: str
    amount: BigDecimal
    status: BookingStatus
    paymentReference: str
    id: Annotated[Long, Id, GeneratedValue] = None  # <2> <3>
