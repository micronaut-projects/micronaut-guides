package example.micronaut.chess.dto

import com.fasterxml.jackson.annotation.JsonValue

enum Player {
    WHITE("w"),
    BLACK("b");

    private final String color

    Player(String color) {
        this.color = color
    }

    @JsonValue
    String toString() {
        this.color
    }
}