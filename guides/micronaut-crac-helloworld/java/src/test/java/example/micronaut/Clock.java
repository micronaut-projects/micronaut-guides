package example.micronaut;

import jakarta.inject.Singleton;

import java.time.LocalDateTime;

@Singleton
class Clock {
    private final LocalDateTime now;

    Clock() {
        now = LocalDateTime.now();
    }

    LocalDateTime getNow() {
        return now;
    }
}