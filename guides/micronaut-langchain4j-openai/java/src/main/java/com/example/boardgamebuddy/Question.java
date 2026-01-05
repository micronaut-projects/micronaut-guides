package com.example.boardgamebuddy;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Question(String question) {
}
