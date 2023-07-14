package example.micronaut;

import example.micronaut.model.BookAvailability;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import jakarta.validation.constraints.NotBlank;

@MappedEntity("book") // <1>
public record BookEntity(
    @Nullable @Id @GeneratedValue(GeneratedValue.Type.AUTO) Long id, // <2>
    @NonNull @NotBlank String name,
    @NonNull @NotBlank BookAvailability availability,
    @Nullable String author,
    @Nullable String isbn) {
}
