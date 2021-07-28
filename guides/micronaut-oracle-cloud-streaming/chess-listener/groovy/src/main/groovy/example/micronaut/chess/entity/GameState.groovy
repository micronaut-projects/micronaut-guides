package example.micronaut.chess.entity

import example.micronaut.chess.dto.GameStateDTO
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.data.annotation.DateCreated
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

import java.time.LocalDateTime

import static io.micronaut.data.annotation.Relation.Kind.MANY_TO_ONE

/**
 * Represents the state of a chess game after a move.
 */
@MappedEntity('GAME_STATE')
@CompileStatic
class GameState {

    @Id
    @NotNull
    final UUID id

    @Relation(MANY_TO_ONE)
    @NotNull
    final Game game

    @DateCreated
    LocalDateTime dateCreated

    @Size(max = 1)
    @NotNull
    final String player

    @Size(max = 100)
    @NotNull
    final String fen

    @NotNull
    final String pgn

    @Size(max = 10)
    @NotNull
    final String move

    /**
     * @param id the id
     * @param game the game
     * @param player b or w
     * @param move the current move
     * @param fen https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
     * @param pgn https://en.wikipedia.org/wiki/Portable_Game_Notation
     */
    GameState(@NonNull UUID id,
              @NonNull Game game,
              @NonNull String player,
              @NonNull String move,
              @NonNull String fen,
              @NonNull String pgn) {
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
