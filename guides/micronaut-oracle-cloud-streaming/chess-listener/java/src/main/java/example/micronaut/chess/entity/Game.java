package example.micronaut.chess.entity;

import example.micronaut.chess.dto.Player;
import example.micronaut.chess.dto.GameDTO;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedEntity("GAME")
public class Game {

    @Id
    @NotNull
    @NonNull
    private final UUID id;

    @Size(max = 255)
    @NonNull
    private final String blackName;

    @Size(max = 255)
    @NonNull
    private final String whiteName;

    @DateCreated
    private LocalDateTime dateCreated;

    @DateUpdated
    private LocalDateTime dateUpdated;

    private boolean draw;

    @Nullable
    @Size(max = 1)
    private Player winner;

    public Game(@NonNull UUID id,
                @NonNull String blackName,
                @NonNull String whiteName) {
        this.id = id;
        this.blackName = blackName;
        this.whiteName = whiteName;
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    @NonNull
    public String getBlackName() {
        return blackName;
    }

    @NonNull
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

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    @NonNull
    public GameDTO toDto() {
        return new GameDTO(id.toString(), blackName, whiteName, draw, winner);
    }
}
