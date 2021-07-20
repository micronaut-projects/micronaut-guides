package example.micronaut.chess;

import example.micronaut.chess.dto.GameDTO;
import example.micronaut.chess.dto.GameStateDTO;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.reactivex.Single;

@KafkaClient // <1>
public interface GameReporter {

    /**
     * Send a game indicating the start or finish (checkmate or draw).
     *
     * @param gameId the game id
     * @param game the game DTO
     * @return a reactive Single to trigger non-blocking send
     */
    @Topic("chessGame") // <2>
    Single<GameDTO> game(@KafkaKey String gameId, GameDTO game); // <3> <4>

    /**
     * Send a game move.
     *
     * @param gameId the game id
     * @param gameState the current state
     * @return a reactive Single to trigger non-blocking send
     */
    @Topic("chessGameState") // <2>
    Single<GameStateDTO> gameState(@KafkaKey String gameId, GameStateDTO gameState); // <3> <4>
}
