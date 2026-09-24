from abc import ABC, abstractmethod
from time import monotonic, sleep
from typing import Annotated
from uuid import uuid4

import pytest
from java.util import UUID
from micronaut.configuration.kafka.annotation import KafkaClient, KafkaKey, Topic
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.chess.dto.game_dto import GameDTO
from example.micronaut.chess.dto.game_state_dto import GameStateDTO
from example.micronaut.chess.dto.player import Player


@KafkaClient
class GameReporter(ABC):

    @Topic("chessGame")
    @abstractmethod
    def game(self, game_id: Annotated[str, KafkaKey], game: GameDTO) -> None:
        ...

    @Topic("chessGameState")
    @abstractmethod
    def game_state(
        self,
        game_id: Annotated[str, KafkaKey],
        game_state: GameStateDTO,
    ) -> None:
        ...


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["test"],
            properties={
                "oci.config.enabled": "false",
            },
            start_application=False,
            transactional=False,
        ),
    )  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def game_reporter(my_context):
    return my_context["example.micronaut.GameReporter"]  # <5>


@pytest.fixture
def game_repository(my_context):
    repository = my_context["example.micronaut.chess.repository.GameRepository"]
    yield repository
    repository.deleteAll()


@pytest.fixture
def game_state_repository(my_context):
    repository = my_context["example.micronaut.chess.repository.GameStateRepository"]
    yield repository
    repository.deleteAll()


def wait_for(condition, timeout: float = 10.0):  # <6>
    deadline = monotonic() + timeout
    while monotonic() < deadline:
        if condition():
            return
        sleep(0.1)
    raise AssertionError("Timed out waiting for Kafka message")


def find_required(repository, id, method="findById"):
    result = getattr(repository, method)(id)
    assert result.isPresent()
    return result.get()


def make_move(
    game_reporter,
    game_id: str,
    player: Player,
    move: str,
    fen: str,
    pgn: str,
) -> UUID:
    game_state_id = uuid4()
    game_reporter.game_state(
        game_id,
        GameStateDTO(str(game_state_id), game_id, player, move, fen, pgn),
    )
    return UUID.fromString(str(game_state_id))


def test_game_ending_in_checkmate(
    game_reporter,
    game_repository,
    game_state_repository,
):
    game_id = uuid4()
    game_id_string = str(game_id)

    game_reporter.game(game_id_string, GameDTO(game_id_string, "b_name", "w_name"))

    wait_for(lambda: game_repository.count() > 0)

    game = find_required(game_repository, UUID.fromString(game_id_string))
    assert game.black_name == "b_name"
    assert game.white_name == "w_name"
    assert not game.draw
    assert game.winner is None

    game_state_ids = [
        make_move(game_reporter, game_id_string, Player.WHITE, "f3", "fen1", "1. f3"),
        make_move(game_reporter, game_id_string, Player.BLACK, "e6", "fen2", "1. f3 e6"),
        make_move(game_reporter, game_id_string, Player.WHITE, "g4", "fen3", "1. f3 e6 2. g4"),
        make_move(game_reporter, game_id_string, Player.BLACK, "Qh4#", "fen4", "1. f3 e6 2. g4 Qh4#"),
    ]

    wait_for(lambda: game_state_repository.count() == 4)

    moves = [find_required(game_state_repository, id, "getById") for id in game_state_ids]
    assert moves[0].player == Player.WHITE
    assert moves[0].move == "f3"
    assert moves[3].player == Player.BLACK
    assert moves[3].move == "Qh4#"

    game_reporter.game(game_id_string, GameDTO(game_id_string, winner=Player.BLACK))

    wait_for(
        lambda: find_required(
            game_repository,
            UUID.fromString(game_id_string),
        ).winner is not None
    )

    game = find_required(game_repository, UUID.fromString(game_id_string))
    assert not game.draw
    assert game.winner == Player.BLACK


def test_game_ending_in_draw(
    game_reporter,
    game_repository,
    game_state_repository,
):
    game_id = uuid4()
    game_id_string = str(game_id)

    game_reporter.game(game_id_string, GameDTO(game_id_string, "b_name", "w_name"))

    wait_for(lambda: game_repository.count() > 0)

    make_move(game_reporter, game_id_string, Player.WHITE, "f3", "fen1", "1. f3")
    make_move(game_reporter, game_id_string, Player.BLACK, "e6", "fen2", "1. f3 e6")

    wait_for(lambda: game_state_repository.count() == 2)

    game_reporter.game(game_id_string, GameDTO(game_id_string, draw=True))

    wait_for(
        lambda: find_required(
            game_repository,
            UUID.fromString(game_id_string),
        ).draw
    )

    game = find_required(game_repository, UUID.fromString(game_id_string))
    assert game.draw
    assert game.winner is None
