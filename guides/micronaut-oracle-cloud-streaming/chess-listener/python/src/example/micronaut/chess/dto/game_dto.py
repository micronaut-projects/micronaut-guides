from dataclasses import dataclass
from typing import Annotated

from jakarta.validation.constraints import NotNull, Size
from micronaut.serde.annotation import Serdeable

from .player import Player


@Serdeable  # <1>
@dataclass(frozen=True)
class GameDTO:
    id: Annotated[str, NotNull, Size(max=36)]
    black_name: Annotated[str | None, Size(max=255)] = None
    white_name: Annotated[str | None, Size(max=255)] = None
    draw: bool = False
    winner: Annotated[Player | None, Size(max=1)] = None
