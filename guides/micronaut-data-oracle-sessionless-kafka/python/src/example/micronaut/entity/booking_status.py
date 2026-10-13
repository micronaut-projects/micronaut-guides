from enum import Enum

from micronaut.serde.annotation import Serdeable


@Serdeable
class BookingStatus(Enum):
    PENDING = "PENDING"
    CONFIRMED = "CONFIRMED"
