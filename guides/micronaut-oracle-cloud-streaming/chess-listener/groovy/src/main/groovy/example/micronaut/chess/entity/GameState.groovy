package example.micronaut.chess.entity

import example.micronaut.chess.dto.GameStateDTO
import groovy.transform.CompileStatic
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation

import java.time.LocalDateTime

import static io.micronaut.data.annotation.Relation.Kind.MANY_TO_ONE

/**
 * Represents the state of a chess game after a move.
 */
@MappedEntity('GAME_STATE')
@CompileStatic
class GameState {

    @Id
    final UUID id

    @Relation(MANY_TO_ONE)
    final Game game

    @DateCreated
    LocalDateTime dateCreated

    final String player

    final String fen

    final String pgn

    final String move

    /**
     * @param id the id
     * @param game the game
     * @param player b or w
     * @param move the current move
     * @param fen https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
     * @param pgn https://en.wikipedia.org/wiki/Portable_Game_Notation
     */
    GameState(UUID id, Game game, String player,
              String move, String fen, String pgn) {
        this.id = id;
        this.game = game
        this.player = player
        this.move = move
        this.fen = fen
        this.pgn = pgn
    }

    GameStateDTO toDto() {
        new GameStateDTO(id.toString(), game.getId().toString(), player, move, fen, pgn)
    }
}
