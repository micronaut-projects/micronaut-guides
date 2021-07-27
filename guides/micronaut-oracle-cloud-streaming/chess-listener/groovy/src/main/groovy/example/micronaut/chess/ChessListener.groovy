package example.micronaut.chess

import example.micronaut.chess.dto.GameDTO
import example.micronaut.chess.dto.GameStateDTO
import groovy.transform.CompileStatic
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic

import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST

@KafkaListener(offsetReset = EARLIEST) // <1>
@CompileStatic
class ChessListener {

    private final GameService gameService

    ChessListener(GameService gameService) { // <2>
        this.gameService = gameService
    }

    @Topic('chessGame') // <3>
    void onGame(GameDTO gameDTO) {
        if (gameDTO.draw) {
            gameService.draw gameDTO // <4>
        }
        else if (gameDTO.winner) {
            gameService.checkmate gameDTO // <5>
        }
        else {
            gameService.newGame gameDTO // <6>
        }
    }

    @Topic('chessGameState') // <3>
    void onGameState(GameStateDTO gameState) {
        gameService.newGameState gameState // <7>
    }
}
