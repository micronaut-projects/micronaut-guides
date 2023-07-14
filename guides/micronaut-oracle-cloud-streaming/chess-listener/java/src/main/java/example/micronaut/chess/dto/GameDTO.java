package example.micronaut.chess.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.micronaut.core.annotation.Creator;
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Serdeable // <1>
@JsonTypeInfo(use = NAME, property = "_className") // <2>
public class GameDTO {

    @Size(max = 36)
    @NotNull
    @NonNull
    private final String id;

    @Size(max = 255)
    @Nullable
    private final String blackName;

    @Size(max = 255)
    @Nullable
    private final String whiteName;

    private final boolean draw;

    @Size(max = 1)
    @Nullable
    private final Player winner;

    @Creator // <3>
    public GameDTO(@NonNull String id,
                   @Nullable String blackName,
                   @Nullable String whiteName,
                   boolean draw,
                   @Nullable Player winner) {
        this.id = id;
        this.blackName = blackName;
        this.whiteName = whiteName;
        this.draw = draw;
        this.winner = winner;
    }

    public GameDTO(@NonNull String id,
                   @NonNull String blackName,
                   @NonNull String whiteName) {
        this(id, blackName, whiteName, false, null);
    }

    public GameDTO(@NonNull String id,
                   boolean draw,
                   @Nullable Player winner) {
        this(id, null, null, draw, winner);
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getBlackName() {
        return blackName;
    }

    @Nullable
    public String getWhiteName() {
        return whiteName;
    }

    public boolean isDraw() {
        return draw;
    }

    @Nullable
    public Player getWinner() {
        return winner;
    }
}
