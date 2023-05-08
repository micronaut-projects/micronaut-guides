package example.micronaut.chess.entity

import example.micronaut.chess.dto.GameDTO
import io.micronaut.core.annotation.Nullable
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.DateUpdated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import java.time.LocalDateTime
import java.util.UUID
import jakarta.validation.constraints.Size

@MappedEntity("GAME")
class Game(

    @field:Id
    val id: UUID,

    @field:Size(max = 255)
    val blackName: String,

    @field:Size(max = 255)
    val whiteName: String) {

    @DateCreated
    var dateCreated: LocalDateTime? = null

    @DateUpdated
    var dateUpdated: LocalDateTime? = null

    var draw = false

    @Nullable
    @field:Size(max = 1) var winner: String? = null

    fun toDto() = GameDTO(id.toString(), blackName, whiteName, draw, winner)
}
