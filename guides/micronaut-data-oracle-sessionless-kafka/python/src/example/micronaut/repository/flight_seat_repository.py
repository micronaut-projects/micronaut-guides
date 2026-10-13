from java.util import Optional
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from ..entity.flight_seat import FlightSeat


@JdbcRepository(dialect=Dialect.ORACLE)
class FlightSeatRepository(CrudRepository[FlightSeat, str]):
    def findByIdForUpdate(self, id: str) -> Optional[FlightSeat]: ...
