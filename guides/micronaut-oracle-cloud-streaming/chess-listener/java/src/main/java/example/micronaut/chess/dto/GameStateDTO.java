package example.micronaut.chess.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.core.annotation.NonNull;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Serdeable // <1>
@JsonTypeInfo(use = NAME, property = "_className") // <2>
public class GameStateDTO {

    @Size(max = 36)
    @NotNull
    @NonNull
    private final String id;

    @Size(max = 36)
    @NotNull
    @NonNull
    private final String gameId;

    @Size(max = 1)
    @NotNull
    @NonNull
    private final Player player;

    @Size(max = 100)
    @NotNull
    @NonNull
    private final String fen;

    @NotNull
    @NonNull
    private final String pgn;

    @Size(max = 10)
    @NotNull
    @NonNull
    private final String move;

    public GameStateDTO(@NonNull String id,
                        @NonNull String gameId,
                        @NonNull Player player,
                        @NonNull String move,
                        @NonNull String fen,
                        @NonNull String pgn) {
        this.id = id;
        this.gameId = gameId;
        this.player = player;
        this.move = move;
        this.fen = fen;
        this.pgn = pgn;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getGameId() {
        return gameId;
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
}
