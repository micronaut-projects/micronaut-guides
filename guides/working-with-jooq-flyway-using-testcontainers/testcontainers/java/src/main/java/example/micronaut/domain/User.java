package example.micronaut.domain;

public record User(
        Long id,
        String name,
        String email
) {}
