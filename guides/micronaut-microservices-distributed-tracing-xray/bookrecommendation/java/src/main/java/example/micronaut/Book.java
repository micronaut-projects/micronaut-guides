package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record Book(String isbn, String name) {
}
