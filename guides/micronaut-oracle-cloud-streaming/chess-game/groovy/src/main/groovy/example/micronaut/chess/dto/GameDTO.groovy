package example.micronaut.chess.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME

/**
 * DTO for <code>Game</code> entity.
 */
@Introspected
@JsonTypeInfo(use = NAME, property = '_className')
@CompileStatic
class GameDTO {

    final String id
    final String blackName
    final String whiteName
    final boolean draw
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
    @Creator
    GameDTO(String id, String blackName, String whiteName,
            boolean draw, String winner) {
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
    GameDTO(String id, String blackName, String whiteName) {
        this(id, blackName, whiteName, false, null)
    }

    /**
     * Constructor for declaring a draw or winner.
     *
     * @param id the game id
     * @param draw <code>true</code> if the game ended in a draw
     * @param winner <code>null</code> if the game ended in a draw, "b", or "w" to indicate the winner
     */
    GameDTO(String id, boolean draw, String winner) {
        this(id, null, null, draw, winner)
    }
}
