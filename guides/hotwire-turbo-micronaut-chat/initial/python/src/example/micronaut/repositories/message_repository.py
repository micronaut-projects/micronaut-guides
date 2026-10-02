from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from ..entities.message import Message
from ..entities.room import Room


# tag::clazz[]
@JdbcRepository(dialect=Dialect.MYSQL)  # <1>
class MessageRepository(CrudRepository[Message, int]):  # <2>
    def findByRoom(self, room: Room) -> list[Message]: ...
# end::clazz[]
