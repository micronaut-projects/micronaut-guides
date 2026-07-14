package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Greeting(long id, String content) { }

