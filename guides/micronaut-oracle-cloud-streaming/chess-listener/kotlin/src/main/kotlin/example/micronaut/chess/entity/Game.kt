package example.micronaut.chess.entity

import example.micronaut.chess.dto.GameDTO
import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.util.Date

/**
 * Represents a chess game.
 */
@MappedEntity("GAME")
class Game(
    @field:Id val id: String,
    val blackName: String,
    val whiteName: String) {

    @DateCreated
    var dateCreated: Date? = null

    @DateUpdated
    var dateUpdated: Date? = null

    var draw = false

    @Nullable
    var winner: String? = null

    fun toDto() = GameDTO(id, blackName, whiteName, draw, winner)
}
