package example.micronaut.chess.entity

import example.micronaut.chess.dto.GameStateDTO
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation
import io.micronaut.data.annotation.Relation.Kind.MANY_TO_ONE
import java.time.LocalDateTime
import java.util.UUID
import jakarta.validation.constraints.Size

@MappedEntity("GAME_STATE")
class GameState(

    @field:Id
    val id: UUID,

    @field:Relation(MANY_TO_ONE)
    val game: Game,

    @field:Size(max = 1)
    val player: String,

    @field:Size(max = 10)
    val move: String,

    @field:Size(max = 100)
    val fen: String,

    val pgn: String) {

    @DateCreated
    var dateCreated: LocalDateTime? = null

    fun toDto() = GameStateDTO(id.toString(), game.id.toString(), player, move, fen, pgn)
}
