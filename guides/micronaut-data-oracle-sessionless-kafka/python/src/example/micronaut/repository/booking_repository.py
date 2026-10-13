from java.lang import Long
from java.util import Optional
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from ..entity.booking import Booking


@JdbcRepository(dialect=Dialect.ORACLE)
class BookingRepository(CrudRepository[Booking, Long]):
    pass
