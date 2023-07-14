package example.micronaut.chess.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.Size

@Serdeable // <1>
@JsonTypeInfo(use = NAME, property = "_className") // <2>
data class GameStateDTO(@field:Size(max = 36) val id: String,
                        @field:Size(max = 36) val gameId: String,
                        @field:Size(max = 1) val player: String,
                        @field:Size(max = 100) val move: String,
                        val fen: String,
                        @field:Size(max = 10) val pgn: String)
