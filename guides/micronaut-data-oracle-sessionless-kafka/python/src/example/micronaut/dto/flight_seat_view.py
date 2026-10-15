from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable

from ..entity.seat_status import SeatStatus


@Serdeable  # <1>
@dataclass
class FlightSeatView:
    id: str
    status: SeatStatus
