package example.micronaut.chess.entity;

import example.micronaut.chess.dto.Player;
import example.micronaut.chess.dto.GameStateDTO;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Relation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

import static io.micronaut.data.annotation.Relation.Kind.MANY_TO_ONE;

@MappedEntity("GAME_STATE")
public class GameState {

    @Id
    @NotNull
    private final UUID id;

    @Relation(MANY_TO_ONE)
    @NotNull
    private final Game game;

    @DateCreated
    private LocalDateTime dateCreated;

    @Size(max = 1)
    @NotNull
    private final Player player;

    // https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
    @Size(max = 100)
    @NotNull
    private final String fen;

    // https://en.wikipedia.org/wiki/Portable_Game_Notation
    @NotNull
    private final String pgn;

    @Size(max = 10)
    @NotNull
    private final String move;

    public GameState(@NonNull UUID id,
                     @NonNull Game game,
                     @NonNull Player player,
                     @NonNull String move,
                     @NonNull String fen,
                     @NonNull String pgn) {
        this.id = id;
        this.game = game;
        this.player = player;
        this.move = move;
        this.fen = fen;
        this.pgn = pgn;
    }

    public UUID getId() {
        return id;
    }

    public Game getGame() {
        return game;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Player getPlayer() {
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
        return new GameStateDTO(id.toString(), game.getId().toString(), player, move, fen, pgn);
    }
}
