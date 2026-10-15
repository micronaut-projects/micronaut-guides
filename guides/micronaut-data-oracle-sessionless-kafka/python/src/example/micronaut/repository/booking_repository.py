from java.lang import Long
from java.util import Optional
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from ..entity.booking import Booking
from ..entity.booking_status import BookingStatus


@JdbcRepository(dialect=Dialect.ORACLE)  # <1>
class BookingRepository(CrudRepository[Booking, Long]):  # <2>
    def findByIdAndStatus(self, id: Long, status: BookingStatus) -> Optional[Booking]: ...
