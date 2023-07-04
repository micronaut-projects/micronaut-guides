package example.micronaut.chess

import example.micronaut.chess.dto.GameDTO
import example.micronaut.chess.dto.GameStateDTO
import example.micronaut.chess.entity.Game
import example.micronaut.chess.entity.GameState
import example.micronaut.chess.repository.GameRepository
import example.micronaut.chess.repository.GameStateRepository
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton
import jakarta.transaction.Transactional

@Singleton
@Transactional
@CompileStatic
class GameService {

    private final Logger log = LoggerFactory.getLogger(GameService.name)

    private final GameRepository gameRepository
    private final GameStateRepository gameStateRepository

    GameService(GameRepository gameRepository,
                GameStateRepository gameStateRepository) {
        this.gameRepository = gameRepository
        this.gameStateRepository = gameStateRepository
    }

    /**
     * Create a new <code>Game</code> and persist it.
     *
     * @param gameDTO the <code>Game</code> data
     * @return the game
     */
    Game newGame(GameDTO gameDTO) {
        log.debug('New game {}, black: {}, white: {}',
                gameDTO.id, gameDTO.blackName, gameDTO.whiteName)
        Game game = new Game(UUID.fromString(gameDTO.id), gameDTO.blackName, gameDTO.whiteName)
        gameRepository.save game
    }

    /**
     * Persist a game move as a <code>GameState</code>.
     *
     * @param gameStateDTO the <code>GameState</code> data
     */
    void newGameState(GameStateDTO gameStateDTO) {
        Game game = findGame(gameStateDTO.gameId)
        GameState gameState = new GameState(
                UUID.fromString(gameStateDTO.id), game,
                gameStateDTO.player, gameStateDTO.move,
                gameStateDTO.fen, gameStateDTO.pgn)
        gameStateRepository.save gameState
    }

    /**
     * Record that a game ended in checkmate.
     *
     * @param gameDTO the <code>Game</code> data
     */
    void checkmate(GameDTO gameDTO) {
        log.debug('Game {} ended with winner: {}',
                gameDTO.id, gameDTO.winner)
        Game game = findGame(gameDTO.id)
        game.winner = gameDTO.winner
        gameRepository.update game
    }

    /**
     * Record that a game ended in a draw.
     *
     * @param gameDTO the <code>Game</code> data
     */
    void draw(GameDTO gameDTO) {
        log.debug('Game {} ended in a draw', gameDTO.id)
        Game game = findGame(gameDTO.id)
        game.draw = true
        gameRepository.update game
    }

    @NonNull
    private Game findGame(String gameId) {
        gameRepository.findById(UUID.fromString(gameId)).orElseThrow(() ->
                new IllegalArgumentException("Game with id '" + gameId + "' not found"))
    }
}
