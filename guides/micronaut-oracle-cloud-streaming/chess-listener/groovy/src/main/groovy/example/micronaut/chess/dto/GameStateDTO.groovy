package example.micronaut.chess.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.Size

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME

@Serdeable // <1>
@JsonTypeInfo(use = NAME, property = '_className') // <2>
@CompileStatic
class GameStateDTO {

    @Size(max = 36)
    @NotBlank
    @NonNull
    final String id

    @Size(max = 36)
    @NotBlank
    @NonNull
    final String gameId

    @Size(max = 1)
    @NotBlank  
    @NonNull  
    final Player player

    @Size(max = 100)
    @NotBlank
    @NonNull
    final String fen

    @NotBlank
    @NonNull
    final String pgn

    @Size(max = 10)
    @NotBlank
    @NonNull
    final String move

    GameStateDTO(@NonNull String id,
                 @NonNull String gameId,
                 @NonNull Player player,
                 @NonNull String move,
                 @NonNull String fen,
                 @NonNull String pgn) {
        this.id = id
        this.gameId = gameId
        this.player = player
        this.move = move
        this.fen = fen
        this.pgn = pgn
    }
}
