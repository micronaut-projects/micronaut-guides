package example.micronaut

import io.micronaut.core.annotation.NonNull
import groovy.transform.EqualsAndHashCode
import io.micronaut.core.annotation.Introspected
import jakarta.validation.constraints.NotBlank

@EqualsAndHashCode
@Introspected
class Book {
    @NonNull
    @NotBlank
    private final String isbn

    @NonNull
    @NotBlank
    private final String name

    Book(@NonNull @NotBlank String isbn, @NonNull @NotBlank String name) {
        this.isbn = isbn
        this.name = name
    }

    @NonNull
    String getIsbn() {
        return isbn
    }

    @NonNull
    String getName() {
        return name
    }
}