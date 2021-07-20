package example.micronaut.chess.entity;

import example.micronaut.chess.dto.GameDTO;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import java.util.Date;

import static io.micronaut.data.annotation.GeneratedValue.Type.IDENTITY;

/**
 * Represents a chess game.
 */
@MappedEntity("GAME")
public class Game {

    @Id
    private final String id;

    private final String blackName;

    private final String whiteName;

    @DateCreated
    private Date dateCreated;

    @DateUpdated
    private Date dateUpdated;

    private boolean draw;

    @Nullable
    private String winner;

    public Game(String id, String blackName, String whiteName) {
        this.id = id;
        this.blackName = blackName;
        this.whiteName = whiteName;
    }

    public String getId() {
        return id;
    }

    public String getBlackName() {
        return blackName;
    }

    public String getWhiteName() {
        return whiteName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
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
        return new GameDTO(id, blackName, whiteName, draw, winner);
    }
}
