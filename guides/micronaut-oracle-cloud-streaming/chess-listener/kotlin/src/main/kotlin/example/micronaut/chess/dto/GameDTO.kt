package example.micronaut.chess.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected

/**
 * DTO for `Game` entity.
 */
@Introspected
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "_className")
class GameDTO

    /**
     * Full constructor.
     *
     * @param id the game id
     * @param blackName the black player name
     * @param whiteName the white player name
     * @param draw `true` if the game ended in a draw
     * @param winner `null` if a new game or the game ended in a draw, "b", or "w" to indicate the winner
     */
    @Creator
    constructor(val id: String, val blackName: String?, val whiteName: String?,
                val draw: Boolean, val winner: String?) {

    /**
     * Constructor for a new game.
     *
     * @param id the game id
     * @param blackName the black player name
     * @param whiteName the white player name
     */
    constructor(id: String, blackName: String, whiteName: String) :
            this(id, blackName, whiteName, false, null)

    /**
     * Constructor for declaring a draw or winner.
     *
     * @param id the game id
     * @param draw `true` if the game ended in a draw
     * @param winner `null` if the game ended in a draw, "b", or "w" to indicate the winner
     */
    constructor(id: String, draw: Boolean, winner: String) :
            this(id, null, null, draw, winner)
}
