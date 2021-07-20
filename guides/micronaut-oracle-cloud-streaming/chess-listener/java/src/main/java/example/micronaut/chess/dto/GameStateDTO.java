package example.micronaut.chess.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.micronaut.core.annotation.Introspected;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

/**
 * DTO for <code>GameState</code> entity.
 */
@Introspected
@JsonTypeInfo(use = NAME, property = "_className")
public class GameStateDTO {

    private final String id;
    private final String gameId;
    private final String player;
    private final String fen;
    private final String pgn;
    private final String move;

    public GameStateDTO(String id, String gameId, String player,
                        String move, String fen, String pgn) {
        this.id = id;
        this.gameId = gameId;
        this.player = player;
        this.move = move;
        this.fen = fen;
        this.pgn = pgn;
    }

    public String getId() {
        return id;
    }

    public String getGameId() {
        return gameId;
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
}
