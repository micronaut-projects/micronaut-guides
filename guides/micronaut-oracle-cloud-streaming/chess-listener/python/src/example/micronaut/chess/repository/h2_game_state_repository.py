from micronaut.context.annotation import Primary, Requires
from micronaut.context.env import Environment
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect

from .game_state_repository import GameStateRepository


@Primary
@JdbcRepository(dialect=Dialect.H2)
@Requires(env=Environment.DEVELOPMENT)
class H2GameStateRepository(GameStateRepository):
    pass
