package example.micronaut.chess.entity;

import example.micronaut.chess.dto.Player;
import example.micronaut.chess.dto.GameStateDTO;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Relation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

import static io.micronaut.data.annotation.Relation.Kind.MANY_TO_ONE;

@MappedEntity("GAME_STATE")
public class GameState {

    @Id
    @NotNull
    @NonNull
    private final UUID id;

    @Relation(MANY_TO_ONE)
    @NotNull
    @NonNull
    private final Game game;

    @DateCreated
    private LocalDateTime dateCreated;

    @Size(max = 1)
    @NotNull
    @NonNull
    private final Player player;

    // https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
    @Size(max = 100)
    @NotNull
    @NonNull
    private final String fen;

    // https://en.wikipedia.org/wiki/Portable_Game_Notation
    @NotNull
    @NonNull
    private final String pgn;

    @Size(max = 10)
    @NotNull
    @NonNull
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

    @NonNull
    public UUID getId() {
        return id;
    }

    @NonNull
    public Game getGame() {
        return game;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    @NonNull
    public Player getPlayer() {
        return player;
    }

    @NonNull
    public String getFen() {
        return fen;
    }

    @NonNull
    public String getPgn() {
        return pgn;
    }

    @NonNull
    public String getMove() {
        return move;
    }

    @NonNull
    public GameStateDTO toDto() {
        return new GameStateDTO(id.toString(), game.getId().toString(), player, move, fen, pgn);
    }
}
