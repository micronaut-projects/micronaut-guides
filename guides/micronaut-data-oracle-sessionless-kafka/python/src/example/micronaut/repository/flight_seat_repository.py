from java.util import Optional
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from ..entity.flight_seat import FlightSeat
from ..entity.seat_status import SeatStatus


@JdbcRepository(dialect=Dialect.ORACLE)  # <1>
class FlightSeatRepository(CrudRepository[FlightSeat, str]):  # <2>
    def findByIdAndStatusForUpdate(self, id: str, status: SeatStatus) -> Optional[FlightSeat]: ...
    def findByIdAndStatus(self, id: str, status: SeatStatus) -> Optional[FlightSeat]: ...
