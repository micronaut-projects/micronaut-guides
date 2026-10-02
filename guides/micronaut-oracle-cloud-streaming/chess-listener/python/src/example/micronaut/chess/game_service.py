from java.util import UUID
from jakarta.inject import Singleton
from jakarta.transaction import Transactional

from .dto.game_dto import GameDTO
from .dto.game_state_dto import GameStateDTO
from .entity.game import Game
from .entity.game_state import GameState
from .repository.game_repository import GameRepository
from .repository.game_state_repository import GameStateRepository


@Singleton
class GameService:

    def __init__(
        self,
        game_repository: GameRepository,
        game_state_repository: GameStateRepository,
    ):
        self.game_repository = game_repository
        self.game_state_repository = game_state_repository

    @Transactional
    def new_game(self, game_dto: GameDTO) -> Game:
        game = Game(
            UUID.fromString(game_dto.id),
            game_dto.black_name,
            game_dto.white_name,
        )
        return self.game_repository.save(game)

    @Transactional
    def new_game_state(self, game_state_dto: GameStateDTO) -> None:
        game = self._find_game(game_state_dto.game_id)
        game_state = GameState(
            UUID.fromString(game_state_dto.id),
            game,
            game_state_dto.player,
            game_state_dto.move,
            game_state_dto.fen,
            game_state_dto.pgn,
        )
        self.game_state_repository.save(game_state)

    @Transactional
    def checkmate(self, game_dto: GameDTO) -> None:
        game = self._find_game(game_dto.id)
        game.winner = game_dto.winner
        self.game_repository.update(game)

    @Transactional
    def draw(self, game_dto: GameDTO) -> None:
        game = self._find_game(game_dto.id)
        game.draw = True
        self.game_repository.update(game)

    def _find_game(self, game_id: str) -> Game:
        game = self.game_repository.findById(UUID.fromString(game_id))
        if game.isPresent():
            return game.get()
        raise ValueError(f"Game with id '{game_id}' not found")
