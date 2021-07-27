package example.micronaut.chess

import example.micronaut.chess.dto.GameDTO
import example.micronaut.chess.dto.GameStateDTO
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST
import io.micronaut.configuration.kafka.annotation.Topic

@KafkaListener(offsetReset = EARLIEST) // <1>
class ChessListener(private val gameService: GameService) { // <2>

    @Topic("chessGame") // <3>
    fun onGame(gameDTO: GameDTO) {
        if (gameDTO.draw) {
            gameService.draw(gameDTO) // <4>
        } else if (gameDTO.winner != null) {
            gameService.checkmate(gameDTO) // <5>
        } else {
            gameService.newGame(gameDTO) // <6>
        }
    }

    @Topic("chessGameState") // <3>
    fun onGameState(gameState: GameStateDTO) = gameService.newGameState(gameState) // <7>
}
