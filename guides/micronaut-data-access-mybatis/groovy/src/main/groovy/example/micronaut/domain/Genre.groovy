package example.micronaut.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotBlank

@CompileStatic
@Serdeable
class Genre {

    @Nullable
    Long id

    @NonNull
    @NotBlank
    String name

    @NonNull
    @JsonIgnore
    Set<Book> books = []

    Genre(@NonNull @NotBlank String name) {
        this.name = name
    }

    @Override
    String toString() {
        "Genre{id=$id, name='$name', books=$books}"
    }
}
