package example.micronaut.chess.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Size

/**
 * DTO for `Game` entity.
 */
@Introspected // <1>
@JsonTypeInfo(use = NAME, property = "_className") // <2>
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
    @Creator // <3>
    constructor(@field:Size(max = 36) val id: String,
                @field:Size(max = 255) val blackName: String?,
                @field:Size(max = 255) val whiteName: String?,
                val draw: Boolean,
                @field:Size(max = 1) val winner: String?) {

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
    constructor(id: String, draw: Boolean, winner: String?) :
            this(id, null, null, draw, winner)
}
