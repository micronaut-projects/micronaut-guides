package example.micronaut.chess

import example.micronaut.chess.dto.GameDTO
import example.micronaut.chess.dto.GameStateDTO
import io.micronaut.configuration.kafka.annotation.KafkaClient
import io.micronaut.configuration.kafka.annotation.KafkaKey
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.core.annotation.NonNull
import reactor.core.publisher.Mono

@KafkaClient // <1>
interface GameReporter {

    @Topic("chessGame") // <2>
    @NonNull
    fun game(@NonNull @KafkaKey gameId: String, // <4>
             @NonNull game: GameDTO): Mono<GameDTO> // <3>

    @Topic("chessGameState") // <2>
    @NonNull
    fun gameState(@NonNull @KafkaKey gameId: String, // <4>
                  @NonNull gameState: GameStateDTO): Mono<GameStateDTO> // <3>
}
