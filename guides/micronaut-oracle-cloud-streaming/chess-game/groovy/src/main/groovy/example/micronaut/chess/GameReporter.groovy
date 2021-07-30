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

    @Topic('chessGame') // <2>
    @NonNull
    Mono<GameDTO> game(@NonNull @KafkaKey String gameId, // <3> <4>
                       @NonNull GameDTO game)

    @Topic('chessGameState') // <2>
    @NonNull
    Mono<GameStateDTO> gameState(@NonNull @KafkaKey String gameId, // <3> <4>
                                 @NonNull GameStateDTO gameState)
}
