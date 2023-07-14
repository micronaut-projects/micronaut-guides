package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record BookCatalogue(@NotBlank String isbn, @NotBlank String name) { }
