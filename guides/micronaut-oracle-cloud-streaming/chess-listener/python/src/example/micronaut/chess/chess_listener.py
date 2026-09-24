from micronaut.configuration.kafka.annotation import KafkaListener, OffsetReset, Topic

from .dto.game_dto import GameDTO
from .dto.game_state_dto import GameStateDTO
from .game_service import GameService


@KafkaListener(groupId="chess-listener-games", offsetReset=OffsetReset.EARLIEST)  # <1>
class ChessGameListener:

    def __init__(self, game_service: GameService):  # <2>
        self.game_service = game_service

    @Topic("chessGame")  # <3>
    def on_game(self, game_dto: GameDTO) -> None:
        if game_dto.draw:
            self.game_service.draw(game_dto)  # <4>
        elif game_dto.winner is not None:
            self.game_service.checkmate(game_dto)  # <5>
        else:
            self.game_service.new_game(game_dto)  # <6>


@KafkaListener(groupId="chess-listener-moves", offsetReset=OffsetReset.EARLIEST)  # <1>
class ChessMoveListener:

    def __init__(self, game_service: GameService):  # <2>
        self.game_service = game_service

    @Topic("chessGameState")  # <3>
    def on_game_state(self, game_state: GameStateDTO) -> None:
        self.game_service.new_game_state(game_state)  # <7>
