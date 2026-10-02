from java.util import UUID
from micronaut.context.annotation import Requires
from micronaut.context.env import Environment
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from ..entity.game import Game


@JdbcRepository(dialect=Dialect.ORACLE)  # <1>
@Requires(env=[Environment.ORACLE_CLOUD, Environment.TEST])  # <2>
class GameRepository(CrudRepository[Game, UUID]):
    pass
