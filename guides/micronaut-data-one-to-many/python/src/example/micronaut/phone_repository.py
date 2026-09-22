from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from .phone_entity import PhoneEntity


@JdbcRepository(dialect=Dialect.H2)  # <1>
class PhoneRepository(CrudRepository[PhoneEntity, int]):  # <2>
    pass
