package example.micronaut.chess

import example.micronaut.chess.dto.GameDTO
import example.micronaut.chess.dto.GameStateDTO
import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic
import io.reactivex.Single

@KafkaClient // <1>
interface GameReporter {

    /**
     * Send a game indicating the start or finish (checkmate or draw).
     *
     * @param gameId the game id
     * @param game the game DTO
     * @return a reactive Single to trigger non-blocking send
     */
    @Topic("chessGame") // <2>
    fun game(@KafkaKey gameId: String, game: GameDTO): Single<GameDTO> // <3> <4>

    /**
     * Send a game move.
     *
     * @param gameId the game id
     * @param gameState the current state
     * @return a reactive Single to trigger non-blocking send
     */
    @Topic("chessGameState") // <2>
    fun gameState(@KafkaKey gameId: String, gameState: GameStateDTO): Single<GameStateDTO> // <3> <4>
}
