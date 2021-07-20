package example.micronaut.chess.entity;

import example.micronaut.chess.dto.GameStateDTO;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Relation;

import java.util.Date;
import java.util.UUID;

import static io.micronaut.data.annotation.Relation.Kind.MANY_TO_ONE;

/**
 * Represents the state of a chess game after a move.
 */
@MappedEntity("GAME_STATE")
public class GameState {

    @Id
    @AutoPopulated(updateable = false)
    private UUID id;

    @Relation(MANY_TO_ONE)
    private final Game game;

    @DateCreated
    private Date dateCreated;

    private final String player;

    private final String fen;

    private final String pgn;

    private final String move;

    /**
     * @param game the game
     * @param player b or w
     * @param move the current move
     * @param fen https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
     * @param pgn https://en.wikipedia.org/wiki/Portable_Game_Notation
     */
    public GameState(Game game, String player, String move,
                     String fen, String pgn) {
        this.game = game;
        this.player = player;
        this.move = move;
        this.fen = fen;
        this.pgn = pgn;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getPlayer() {
        return player;
    }

    public String getFen() {
        return fen;
    }

    public String getPgn() {
        return pgn;
    }

    public String getMove() {
        return move;
    }

    public GameStateDTO toDto() {
        return new GameStateDTO(id.toString(), game.getId(), player, move, fen, pgn);
    }
}
