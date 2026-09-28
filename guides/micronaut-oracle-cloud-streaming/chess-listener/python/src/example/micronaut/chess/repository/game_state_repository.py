from java.util import Optional, UUID
from micronaut.context.annotation import Requires
from micronaut.context.env import Environment
from micronaut.data.annotation import Join
from micronaut.data.jdbc.annotation import JdbcRepository
from micronaut.data.model.query.builder.sql import Dialect
from micronaut.data.repository import CrudRepository

from ..entity.game_state import GameState


@JdbcRepository(dialect=Dialect.ORACLE)
@Requires(env=[Environment.ORACLE_CLOUD, Environment.TEST])
class GameStateRepository(CrudRepository[GameState, UUID]):

    @Join(value="game", type=Join.Type.FETCH)  # <1>
    def getById(self, id: UUID) -> Optional[GameState]: ...
