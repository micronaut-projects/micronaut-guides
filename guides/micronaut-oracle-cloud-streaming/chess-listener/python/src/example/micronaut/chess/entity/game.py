from dataclasses import dataclass
from typing import Annotated

from java.time import LocalDateTime
from java.util import UUID
from jakarta.validation.constraints import NotNull, Size
from micronaut.core.annotation import Nullable
from micronaut.data.annotation import DateCreated, DateUpdated, Id, MappedEntity

from ..dto.game_dto import GameDTO
from ..dto.player import Player


@dataclass
@MappedEntity("GAME")
class Game:
    id: Annotated[UUID, Id, NotNull]
    black_name: Annotated[str, NotNull, Size(max=255)]
    white_name: Annotated[str, NotNull, Size(max=255)]
    date_created: Annotated[LocalDateTime, Nullable, DateCreated] = None
    date_updated: Annotated[LocalDateTime, Nullable, DateUpdated] = None
    draw: bool = False
    winner: Annotated[Player | None, Size(max=1)] = None

    def to_dto(self) -> GameDTO:
        return GameDTO(
            str(self.id),
            self.black_name,
            self.white_name,
            self.draw,
            self.winner,
        )
