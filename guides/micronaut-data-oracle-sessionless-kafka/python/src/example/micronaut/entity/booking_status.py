from enum import Enum

from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
class BookingStatus(Enum):
    PENDING = "PENDING"
    CONFIRMED = "CONFIRMED"
