from dataclasses import dataclass
from typing import Annotated

from java.time import LocalDateTime
from java.util import UUID
from jakarta.validation.constraints import NotNull, Size
from micronaut.core.annotation import Nullable
from micronaut.data.annotation import DateCreated, Id, MappedEntity, Relation

from ..dto.game_state_dto import GameStateDTO
from ..dto.player import Player
from .game import Game


@dataclass
@MappedEntity("GAME_STATE")
class GameState:
    id: Annotated[UUID, Id, NotNull]
    game: Annotated[Game, Relation(value=Relation.Kind.MANY_TO_ONE), NotNull]
    player: Annotated[Player, NotNull, Size(max=1)]
    move: Annotated[str, NotNull, Size(max=10)]
    fen: Annotated[str, NotNull, Size(max=100)]
    pgn: Annotated[str, NotNull]
    date_created: Annotated[LocalDateTime, Nullable, DateCreated] = None

    def to_dto(self) -> GameStateDTO:
        return GameStateDTO(
            str(self.id),
            str(self.game.id),
            self.player,
            self.move,
            self.fen,
            self.pgn,
        )
