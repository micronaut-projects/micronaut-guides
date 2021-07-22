package example.micronaut.chess.entity

import example.micronaut.chess.dto.GameStateDTO
import io.micronaut.data.annotation.AutoPopulated
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation
import io.micronaut.data.annotation.Relation.Kind.MANY_TO_ONE
import java.util.Date
import java.util.UUID

/**
 * Represents the state of a chess game after a move.
 */
@MappedEntity("GAME_STATE")
class GameState(
    @field:Relation(MANY_TO_ONE) val game: Game,
    val player: String,
    val move: String,
    val fen: String,
    val pgn: String) {

    @Id
    @AutoPopulated(updateable = false)
    var id: UUID? = null

    @DateCreated
    var dateCreated: Date? = null

    fun toDto() = GameStateDTO(id.toString(), game.id, player, move, fen, pgn)
}
