from dataclasses import dataclass

from micronaut.serde.annotation import Serdeable

from ..entity.flight_seat import FlightSeat
from ..entity.seat_status import SeatStatus


@Serdeable
@dataclass
class FlightSeatView:
    id: str
    status: SeatStatus

    @staticmethod
    def from_seat(seat: FlightSeat) -> "FlightSeatView":
        return FlightSeatView(seat.id, seat.status)
