from micronaut.context.annotation import Primary, Requires
from micronaut.context.env import Environment
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect

from .game_state_repository import GameStateRepository


@Primary
@JdbcRepository(dialect=Dialect.ORACLE)
@Requires(
    env=[Environment.ORACLE_CLOUD, Environment.TEST],
    notEnv=Environment.DEVELOPMENT,
)
class OracleGameStateRepository(GameStateRepository):
    pass
