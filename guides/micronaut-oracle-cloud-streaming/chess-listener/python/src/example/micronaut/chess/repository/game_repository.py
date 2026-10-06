from abc import ABC, abstractmethod
from uuid import UUID

from micronaut.data.repository import CrudRepository

from ..entity.game import Game


class GameRepository(CrudRepository[Game, UUID], ABC):
    @abstractmethod
    def getById(self, id: UUID) -> Game | None: ...
