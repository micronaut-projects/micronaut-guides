from dataclasses import dataclass
from typing import Annotated

from micronaut.data.annotation import Id, MappedEntity

from .seat_status import SeatStatus


@MappedEntity("flight_seat")
@dataclass
class FlightSeat:
    id: Annotated[str, Id]
    status: SeatStatus
