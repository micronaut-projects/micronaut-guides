package example.micronaut.chess.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME
import io.micronaut.core.annotation.Introspected

/**
 * DTO for `GameState` entity.
 */
@Introspected
@JsonTypeInfo(use = NAME, property = "_className")
data class GameStateDTO(val id: String, val gameId: String, val player: String,
                        val move: String, val fen: String, val pgn: String)
