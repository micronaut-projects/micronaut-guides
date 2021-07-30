package example.micronaut.chess.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME

/**
 * DTO for <code>Game</code> entity.
 */
@Introspected // <1>
@JsonTypeInfo(use = NAME, property = '_className') // <2>
@CompileStatic
class GameDTO {

    @Size(max = 36)
    @NotNull
    @NonNull
    final String id

    @Size(max = 255)
    @Nullable
    final String blackName

    @Size(max = 255)
    @Nullable
    final String whiteName

    final boolean draw

    @Size(max = 1)
    @Nullable
    final String winner

    /**
     * Full constructor.
     *
     * @param id the game id
     * @param blackName the black player name
     * @param whiteName the white player name
     * @param draw <code>true</code> if the game ended in a draw
     * @param winner <code>null</code> if a new game or the game ended in a draw, "b", or "w" to indicate the winner
     */
    @Creator // <3>
    GameDTO(@NonNull String id,
            @Nullable String blackName,
            @Nullable String whiteName,
            boolean draw,
            @Nullable String winner) {
        this.id = id
        this.blackName = blackName
        this.whiteName = whiteName
        this.draw = draw
        this.winner = winner
    }

    /**
     * Constructor for a new game.
     *
     * @param id the game id
     * @param blackName the black player name
     * @param whiteName the white player name
     */
    GameDTO(@NonNull String id,
            @NonNull String blackName,
            @NonNull String whiteName) {
        this(id, blackName, whiteName, false, null)
    }

    /**
     * Constructor for declaring a draw or winner.
     *
     * @param id the game id
     * @param draw <code>true</code> if the game ended in a draw
     * @param winner <code>null</code> if the game ended in a draw, "b", or "w" to indicate the winner
     */
    GameDTO(@NonNull String id,
            boolean draw,
            @Nullable String winner) {
        this(id, null, null, draw, winner)
    }
}
