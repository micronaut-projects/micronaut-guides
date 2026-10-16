from abc import ABC, abstractmethod
from uuid import UUID

from micronaut.data.annotation import Join
from micronaut.data.repository import CrudRepository

from ..entity.game_state import GameState


class GameStateRepository(CrudRepository[GameState, UUID], ABC):

    @Join(value="game", type=Join.Type.FETCH)  # <1>
    @abstractmethod
    def getById(self, id: UUID) -> GameState | None: ...
