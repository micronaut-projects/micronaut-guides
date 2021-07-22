package example.micronaut.chess.dto

import com.fasterxml.jackson.annotation.JsonTypeInfo
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME

/**
 * DTO for <code>GameState</code> entity.
 */
@Introspected
@JsonTypeInfo(use = NAME, property = '_className')
@CompileStatic
class GameStateDTO {

    final String id
    final String gameId
    final String player
    final String fen
    final String pgn
    final String move

    GameStateDTO(String id, String gameId, String player,
                 String move, String fen, String pgn) {
        this.id = id
        this.gameId = gameId
        this.player = player
        this.move = move
        this.fen = fen
        this.pgn = pgn
    }
}
