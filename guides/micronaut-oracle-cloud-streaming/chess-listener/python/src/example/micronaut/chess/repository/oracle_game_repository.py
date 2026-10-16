from micronaut.context.annotation import Primary, Requires
from micronaut.context.env import Environment
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect

from .game_repository import GameRepository


@Primary
@JdbcRepository(dialect=Dialect.ORACLE)  # <1>
@Requires(
    env=[Environment.ORACLE_CLOUD, Environment.TEST],
    notEnv=Environment.DEVELOPMENT,
)  # <2>
class OracleGameRepository(GameRepository):
    pass
