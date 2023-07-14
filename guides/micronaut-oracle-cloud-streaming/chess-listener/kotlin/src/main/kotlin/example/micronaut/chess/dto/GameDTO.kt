package example.micronaut.chess.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME
import io.micronaut.core.annotation.Creator
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.Size

@Serdeable // <1>
@JsonTypeInfo(use = NAME, property = "_className") // <2>
class GameDTO

    @Creator // <3>
    constructor(@field:Size(max = 36) val id: String,
                @field:Size(max = 255) val blackName: String?,
                @field:Size(max = 255) val whiteName: String?,
                val draw: Boolean,
                @field:Size(max = 1) val winner: String?) {

    constructor(id: String, blackName: String, whiteName: String) :
            this(id, blackName, whiteName, false, null)

    constructor(id: String, draw: Boolean, winner: String?) :
            this(id, null, null, draw, winner)
}
