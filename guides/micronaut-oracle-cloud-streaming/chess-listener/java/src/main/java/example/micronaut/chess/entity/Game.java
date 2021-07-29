package example.micronaut.chess.entity;

import example.micronaut.chess.dto.GameDTO;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a chess game.
 */
@MappedEntity("GAME")
public class Game {

    @Id
    @NotNull
    private final UUID id;

    @Size(max = 255)
    private final String blackName;

    @Size(max = 255)
    private final String whiteName;

    @DateCreated
    private LocalDateTime dateCreated;

    @DateUpdated
    private LocalDateTime dateUpdated;

    private boolean draw;

    @Nullable
    @Size(max = 1)
    private String winner;

    public Game(@NonNull UUID id,
                @NonNull String blackName,
                @NonNull String whiteName) {
        this.id = id;
        this.blackName = blackName;
        this.whiteName = whiteName;
    }

    public UUID getId() {
        return id;
    }

    public String getBlackName() {
        return blackName;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public GameDTO toDto() {
        return new GameDTO(id.toString(), blackName, whiteName, draw, winner);
    }
}
