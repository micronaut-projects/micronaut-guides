from enum import Enum

from micronaut.serde.annotation import Serdeable


@Serdeable  # <1>
class SeatStatus(Enum):
    AVAILABLE = "AVAILABLE"
    HELD = "HELD"
    BOOKED = "BOOKED"
