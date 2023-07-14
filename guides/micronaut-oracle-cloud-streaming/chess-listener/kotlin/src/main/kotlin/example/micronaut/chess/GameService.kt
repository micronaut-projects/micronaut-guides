package example.micronaut.chess

import example.micronaut.chess.dto.GameDTO
import example.micronaut.chess.dto.GameStateDTO
import example.micronaut.chess.entity.Game
import example.micronaut.chess.entity.GameState
import example.micronaut.chess.repository.GameRepository
import example.micronaut.chess.repository.GameStateRepository
import org.slf4j.LoggerFactory
import java.util.UUID
import jakarta.inject.Singleton
import jakarta.transaction.Transactional

@Singleton
@Transactional
open class GameService(private val gameRepository: GameRepository,
                       private val gameStateRepository: GameStateRepository) {

    private val log = LoggerFactory.getLogger(GameService::class.java.name)

    open fun newGame(gameDTO: GameDTO): Game {
        log.debug("New game {}, black: {}, white: {}",
                gameDTO.id, gameDTO.blackName, gameDTO.whiteName)
        val game = Game(UUID.fromString(gameDTO.id), gameDTO.blackName!!, gameDTO.whiteName!!)
        return gameRepository.save(game)
    }

    open fun newGameState(gameStateDTO: GameStateDTO) {
        val game = findGame(gameStateDTO.gameId)
        val gameState = GameState(
                UUID.fromString(gameStateDTO.id), game,
                gameStateDTO.player, gameStateDTO.move,
                gameStateDTO.fen, gameStateDTO.pgn)
        gameStateRepository.save(gameState)
    }

    open fun checkmate(gameDTO: GameDTO) {
        log.debug("Game {} ended with winner: {}", gameDTO.id, gameDTO.winner)
        val game = findGame(gameDTO.id)
        game.winner = gameDTO.winner
        gameRepository.update(game)
    }

    open fun draw(gameDTO: GameDTO) {
        log.debug("Game {} ended in a draw", gameDTO.id)
        val game = findGame(gameDTO.id)
        game.draw = true
        gameRepository.update(game)
    }

    private fun findGame(gameId: String) =
            gameRepository.findById(UUID.fromString(gameId))
                    .orElseThrow { IllegalArgumentException("Game with id '$gameId' not found") }
}
