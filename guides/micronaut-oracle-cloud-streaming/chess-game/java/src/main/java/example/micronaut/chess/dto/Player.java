package example.micronaut.chess.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Player {
    WHITE("w"),
    BLACK("b");

    private final String color;

    Player(String color) {
        this.color = color;
    }

    @JsonValue
    public String toString() {
        return this.color;
    }
}