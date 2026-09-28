from collections import deque
from time import monotonic, sleep

import pytest
import requests
from jakarta.inject import Singleton
from micronaut.configuration.kafka.annotation import KafkaListener, OffsetReset, Topic
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.chess.dto.game_dto import GameDTO
from example.micronaut.chess.dto.game_state_dto import GameStateDTO
from example.micronaut.chess.dto.player import Player


@Singleton
class ReceivedEvents:
    def __init__(self):
        self.games = deque()
        self.moves = deque()

    def clear(self):
        self.games.clear()
        self.moves.clear()


@KafkaListener(groupId="chess-game-tests-games", offsetReset=OffsetReset.EARLIEST)
class ChessGameListener:
    def __init__(self, received_events: ReceivedEvents):
        self.received_events = received_events

    @Topic("chessGame")
    def on_game(self, game: GameDTO):
        self.received_events.games.append(game)


@KafkaListener(groupId="chess-game-tests-moves", offsetReset=OffsetReset.EARLIEST)
class ChessMoveListener:
    def __init__(self, received_events: ReceivedEvents):
        self.received_events = received_events

    @Topic("chessGameState")
    def on_game_state(self, game_state: GameStateDTO):
        self.received_events.moves.append(game_state)


@pytest.fixture
def my_context(request):
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(transactional=False),
    )  # <1>
    yield fixture
    fixture.stop()


@pytest.fixture
def client(my_context):
    return requests.with_context(my_context)  # <6>


@pytest.fixture
def received_events(my_context):
    events = my_context["example.micronaut.ReceivedEvents"]  # <5>
    yield events
    events.clear()


def wait_for(condition, timeout: float = 10.0):  # <7>
    deadline = monotonic() + timeout
    while monotonic() < deadline:
        if condition():
            return
        sleep(0.1)
    raise AssertionError("Timed out waiting for Kafka message")


def start_game(client, black_name: str, white_name: str) -> str:
    response = client.post(
        "/game/start",
        data={Player.BLACK.value: black_name, Player.WHITE.value: white_name},  # <9>
    )  # <10>
    assert response.status_code == 201
    return response.text


def make_move(client, game_id: str, player: Player, move: str, fen: str, pgn: str):
    response = client.post(
        f"/game/move/{game_id}",
        data={
            "player": player.value,
            "move": move,
            "fen": fen,
            "pgn": pgn,
        },
    )  # <11>
    assert response.status_code == 201


def end_game(client, game_id: str, winner: Player | None):
    path = f"/game/draw/{game_id}"
    if winner is not None:
        path = f"/game/checkmate/{game_id}/{winner.value}"
    response = client.post(path)  # <12>
    assert response.status_code == 204


def test_game_ending_in_checkmate(client, received_events):
    game_id = start_game(client, "b_name", "w_name")

    wait_for(lambda: len(received_events.games) == 1)

    game = received_events.games[0]
    assert game.id == game_id
    assert game.black_name == "b_name"
    assert game.white_name == "w_name"
    assert not game.draw
    assert game.winner is None

    received_events.games.clear()

    make_move(client, game_id, Player.WHITE, "f3", "fen1", "1. f3")
    make_move(client, game_id, Player.BLACK, "e6", "fen2", "1. f3 e6")
    make_move(client, game_id, Player.WHITE, "g4", "fen3", "1. f3 e6 2. g4")
    make_move(client, game_id, Player.BLACK, "Qh4#", "fen4", "1. f3 e6 2. g4 Qh4#")

    wait_for(lambda: len(received_events.moves) == 4)

    assert received_events.moves[0].player == Player.WHITE
    assert received_events.moves[0].move == "f3"
    assert received_events.moves[3].player == Player.BLACK
    assert received_events.moves[3].move == "Qh4#"

    received_events.moves.clear()
    end_game(client, game_id, Player.BLACK)

    wait_for(lambda: len(received_events.games) == 1)

    game = received_events.games[0]
    assert game.id == game_id
    assert not game.draw
    assert game.winner == Player.BLACK


def test_game_ending_in_draw(client, received_events):
    game_id = start_game(client, "b_name", "w_name")

    wait_for(lambda: len(received_events.games) == 1)
    received_events.games.clear()

    make_move(client, game_id, Player.WHITE, "f3", "fen1", "1. f3")
    make_move(client, game_id, Player.BLACK, "e6", "fen2", "1. f3 e6")

    wait_for(lambda: len(received_events.moves) == 2)

    received_events.moves.clear()
    end_game(client, game_id, None)

    wait_for(lambda: len(received_events.games) == 1)

    game = received_events.games[0]
    assert game.id == game_id
    assert game.draw
    assert game.winner is None
