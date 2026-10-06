from datetime import datetime
from time import monotonic, sleep
from uuid import UUID, uuid4

import pytest
from pyronaut.test import MicronautTest, micronaut_test_fixture

from example.micronaut.chess.dto.game_dto import GameDTO
from example.micronaut.chess.dto.game_state_dto import GameStateDTO
from example.micronaut.chess.dto.player import Player
from example.micronaut.chess.repository.game_repository import GameRepository
from example.micronaut.chess.repository.game_state_repository import GameStateRepository
from example.micronaut.chess.repository.h2_game_repository import H2GameRepository
from example.micronaut.chess.repository.h2_game_state_repository import H2GameStateRepository
from example.micronaut.chess.repository.oracle_game_repository import OracleGameRepository
from example.micronaut.chess.repository.oracle_game_state_repository import OracleGameStateRepository
from example.micronaut.game_reporter import GameReporter


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
    return my_context[GameReporter]  # <2>


@pytest.fixture
def game_repository(my_context):
    repository = my_context[GameRepository]
    yield repository
    repository.deleteAll()


@pytest.fixture
def game_state_repository(my_context):
    repository = my_context[GameStateRepository]
    yield repository
    repository.deleteAll()


@pytest.fixture
def h2_context(request):
    scope = f"chess_h2_{uuid4().hex}"
    fixture = micronaut_test_fixture(
        request,
        MicronautTest(
            environments=["dev"],
            properties={
                "datasources.default.url": f"jdbc:h2:mem:{scope};DB_CLOSE_DELAY=-1",
                "datasources.default.driver-class-name": "org.h2.Driver",
                "datasources.default.db-type": "h2",
                "datasources.default.dialect": "H2",
                "datasources.default.username": "sa",
                "datasources.default.password": "",
                "flyway.datasources.default.locations": "classpath:db/migration/h2",
                "oci.config.enabled": "false",
                "kafka.bootstrap.servers": "${auto.test.resources.kafka.bootstrap.servers}",
                "micronaut.test.resources.scope": scope,
            },
            start_application=False,
            transactional=False,
        ),
    )
    yield fixture
    fixture.stop()


def wait_for(condition, timeout: float = 10.0):  # <3>
    deadline = monotonic() + timeout
    while monotonic() < deadline:
        if condition():
            return
        sleep(0.1)
    raise AssertionError("Timed out waiting for Kafka message")


def find_required(repository, id, method="getById"):
    result = getattr(repository, method)(id)
    assert result is not None
    return result


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
    return game_state_id


def test_game_ending_in_checkmate(
    game_reporter,
    game_repository,
    game_state_repository,
):
    game_id = uuid4()
    game_id_string = str(game_id)

    game_reporter.game(game_id_string, GameDTO(game_id_string, "b_name", "w_name"))

    wait_for(lambda: game_repository.count() > 0)

    game = find_required(game_repository, game_id)
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
            game_id,
        ).winner is not None
    )

    game = find_required(game_repository, game_id)
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
            game_id,
        ).draw
    )

    game = find_required(game_repository, game_id)
    assert game.draw
    assert game.winner is None


def test_h2_dev_listener_persists_and_joins_game(h2_context):
    assert H2GameRepository in h2_context
    assert H2GameStateRepository in h2_context
    assert OracleGameRepository not in h2_context
    assert OracleGameStateRepository not in h2_context
    game_repository = h2_context[GameRepository]
    game_state_repository = h2_context[GameStateRepository]
    game_reporter = h2_context[GameReporter]
    game_id = uuid4()
    game_id_string = str(game_id)

    assert game_repository.getById(game_id) is None
    assert game_state_repository.getById(uuid4()) is None

    game_reporter.game(game_id_string, GameDTO(game_id_string, "b_name", "w_name"))
    wait_for(lambda: game_repository.getById(game_id) is not None)

    game = find_required(game_repository, game_id)
    assert isinstance(game.id, UUID)
    assert game.id == game_id
    assert isinstance(game.date_created, datetime)
    assert isinstance(game.date_updated, datetime)
    assert game.black_name == "b_name"
    assert game.white_name == "w_name"

    state_id = make_move(
        game_reporter, game_id_string, Player.BLACK, "Qh4#", "fen1", "1. Qh4#"
    )
    wait_for(lambda: game_state_repository.getById(state_id) is not None)
    state = find_required(game_state_repository, state_id)
    assert isinstance(state.id, UUID)
    assert state.id == state_id
    assert isinstance(state.date_created, datetime)
    assert state.player == Player.BLACK
    assert state.move == "Qh4#"
    assert state.game.id == game_id
    assert state.game.black_name == "b_name"
    assert state.game.white_name == "w_name"

    game_reporter.game(game_id_string, GameDTO(game_id_string, winner=Player.BLACK))
    wait_for(lambda: find_required(game_repository, game_id).winner == Player.BLACK)
    game = find_required(game_repository, game_id)
    assert not game.draw
    assert game.winner == Player.BLACK
    assert find_required(game_state_repository, state_id).game.winner == Player.BLACK

    game.black_name = "updated_name"
    game_repository.update(game)
    assert find_required(game_repository, game_id).black_name == "updated_name"
    state.fen = "updated_fen"
    game_state_repository.update(state)
    assert find_required(game_state_repository, state_id).fen == "updated_fen"
    assert find_required(game_state_repository, state_id).game.black_name == "updated_name"

    game_state_repository.deleteById(state_id)
    assert game_state_repository.getById(state_id) is None
    game_repository.deleteById(game_id)
    assert game_repository.getById(game_id) is None
