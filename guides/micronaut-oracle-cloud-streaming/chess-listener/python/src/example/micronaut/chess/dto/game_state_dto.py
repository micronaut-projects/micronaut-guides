from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotNull, Size
from micronaut.serde.annotation import Serdeable

from .player import Player


@Serdeable  # <1>
@dataclass(frozen=True)
class GameStateDTO:
    id: Annotated[str, NotNull, Size(max=36)]
    game_id: Annotated[str, NotNull, Size(max=36)]
    player: Annotated[Player, NotNull, Size(max=1)]
    move: Annotated[str, NotNull, Size(max=10)]
    fen: Annotated[str, NotNull, Size(max=100)]
    pgn: Annotated[str, NotNull]
