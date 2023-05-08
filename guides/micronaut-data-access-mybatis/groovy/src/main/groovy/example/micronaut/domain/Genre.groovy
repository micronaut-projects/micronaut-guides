package example.micronaut.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotNull

@CompileStatic
@Serdeable
class Genre {

    @Nullable
    Long id

    @NotNull
    @NonNull
    String name

    @NonNull
    @JsonIgnore
    Set<Book> books = []

    Genre(@NonNull @NotNull String name) {
        this.name = name
    }

    @Override
    String toString() {
        "Genre{id=$id, name='$name', books=$books}"
    }
}
