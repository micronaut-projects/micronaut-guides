from enum import Enum

from micronaut.serde.annotation import Serdeable


@Serdeable
class SeatStatus(Enum):
    AVAILABLE = "AVAILABLE"
    HELD = "HELD"
    BOOKED = "BOOKED"
