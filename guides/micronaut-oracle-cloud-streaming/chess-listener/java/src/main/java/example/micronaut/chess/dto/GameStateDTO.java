package example.micronaut.chess.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Introspected // <1>
@JsonTypeInfo(use = NAME, property = "_className") // <2>
public class GameStateDTO {

    @Size(max = 36)
    @NotNull
    private final String id;

    @Size(max = 36)
    @NotNull
    private final String gameId;

    @Size(max = 1)
    @NotNull
    private final String player;

    @Size(max = 100)
    @NotNull
    private final String fen;

    @NotNull
    private final String pgn;

    @Size(max = 10)
    @NotNull
    private final String move;

    public GameStateDTO(@NonNull String id,
                        @NonNull String gameId,
                        @NonNull String player,
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
    public String getPlayer() {
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
