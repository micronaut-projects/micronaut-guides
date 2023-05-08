package example.micronaut.domain

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotNull

@CompileStatic
@Serdeable
class Book {

    @Nullable
    Long id

    @NonNull
    @NotNull
    String name

    @NonNull
    @NotNull
    String isbn

    Genre genre

    Book(@NonNull @NotNull String isbn,
         @NonNull @NotNull String name,
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
