package example.micronaut.chess;

import example.micronaut.chess.dto.GameDTO;
import example.micronaut.chess.dto.GameStateDTO;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.context.annotation.Requires;

import static io.micronaut.context.env.Environment.TEST;

@Requires(notEnv = TEST)
@KafkaListener // <1>
class ChessListener {

    private final GameService gameService;

    ChessListener(GameService gameService) { // <2>
        this.gameService = gameService;
    }

    @Topic("chessGame") // <3>
    void onGame(GameDTO gameDTO) {
        if (gameDTO.isDraw()) {
            gameService.draw(gameDTO); // <4>
        }
        else if (gameDTO.getWinner() != null) {
            gameService.checkmate(gameDTO); // <5>
        }
        else {
            gameService.newGame(gameDTO); // <6>
        }
    }

    @Topic("chessGameState") // <3>
    void onGameState(GameStateDTO gameState) {
        gameService.newGameState(gameState); // <7>
    }
}
