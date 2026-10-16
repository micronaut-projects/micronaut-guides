from abc import ABC, abstractmethod
from typing import Annotated

from micronaut.configuration.kafka.annotation import KafkaClient, KafkaKey, Topic

from .dto.game_dto import GameDTO
from .dto.game_state_dto import GameStateDTO


@KafkaClient  # <1>
class GameReporter(ABC):

    @Topic("chessGame")  # <2>
    @abstractmethod
    def game(self, game_id: Annotated[str, KafkaKey], game: GameDTO) -> None:  # <3> <4>
        ...

    @Topic("chessGameState")  # <2>
    @abstractmethod
    def game_state(
        self,
        game_id: Annotated[str, KafkaKey],  # <3> <4>
        game_state: GameStateDTO,
    ) -> None:
        ...
