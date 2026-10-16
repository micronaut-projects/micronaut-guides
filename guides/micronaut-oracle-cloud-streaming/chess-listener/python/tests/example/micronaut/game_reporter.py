from abc import ABC, abstractmethod
from typing import Annotated

from micronaut.configuration.kafka.annotation import KafkaClient, KafkaKey, Topic

from example.micronaut.chess.dto.game_dto import GameDTO
from example.micronaut.chess.dto.game_state_dto import GameStateDTO


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
