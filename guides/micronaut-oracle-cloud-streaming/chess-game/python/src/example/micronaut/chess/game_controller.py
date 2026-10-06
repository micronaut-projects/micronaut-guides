from typing import Annotated
from uuid import uuid4

from micronaut.http import MediaType
from micronaut.http.annotation import Controller, PathVariable, Post, Status
from micronaut.http import HttpStatus
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from .dto.game_dto import GameDTO
from .dto.game_state_dto import GameStateDTO
from .dto.player import Player
from .game_reporter import GameReporter


@Controller("/game")  # <1>
@ExecuteOn(TaskExecutors.BLOCKING)  # <2>
class GameController:

    def __init__(self, game_reporter: GameReporter):  # <2>
        self.game_reporter = game_reporter

    @Post(
        value="/start",  # <3>
        consumes=MediaType.APPLICATION_FORM_URLENCODED,  # <4>
        produces=MediaType.TEXT_PLAIN,  # <5>
    )
    @Status(HttpStatus.CREATED)  # <6>
    def start(self, b: str, w: str) -> str:  # <7>
        game = GameDTO(str(uuid4()), b, w)  # <8>
        self.game_reporter.game(game.id, game)
        return game.id  # <9>

    @Post(
        value="/move/{game_id}",  # <10>
        consumes=MediaType.APPLICATION_FORM_URLENCODED,  # <11>
    )
    @Status(HttpStatus.CREATED)  # <12>
    def move(
        self,
        game_id: Annotated[str, PathVariable],
        player: Player,
        move: str,
        fen: str,
        pgn: str,
    ) -> None:
        game_state = GameStateDTO(str(uuid4()), game_id, player, move, fen, pgn)
        self.game_reporter.game_state(game_id, game_state)  # <13>

    @Post("/checkmate/{game_id}/{player}")  # <14>
    @Status(HttpStatus.NO_CONTENT)  # <15>
    def checkmate(
        self,
        game_id: Annotated[str, PathVariable],
        player: Annotated[Player, PathVariable],
    ) -> None:
        self.game_reporter.game(game_id, GameDTO(game_id, winner=player))  # <16>

    @Post("/draw/{game_id}")  # <17>
    @Status(HttpStatus.NO_CONTENT)  # <18>
    def draw(self, game_id: Annotated[str, PathVariable]) -> None:
        self.game_reporter.game(game_id, GameDTO(game_id, draw=True))  # <19>
