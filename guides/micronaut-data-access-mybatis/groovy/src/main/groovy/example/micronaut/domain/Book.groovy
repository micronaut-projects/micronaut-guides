package example.micronaut.domain

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@CompileStatic
@Serdeable
class Book {

    @Nullable
    Long id

    @NonNull
    @NotBlank
    String name

    @NonNull
    @NotBlank
    String isbn

    Genre genre

    Book(@NonNull @NotBlank String isbn,
         @NonNull @NotBlank String name,
         Genre genre) {
        this.isbn = isbn
        this.name = name
        this.genre = genre
    }

    @Override
    String toString() {
        "Book{id=$id, name='$name', isbn='$isbn', genre=$genre}"
    }
}
