package example.micronaut.genre

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotBlank

@CompileStatic
@Serdeable
class GenreUpdateCommand {

    long id

    @NotBlank
    @NonNull
    String name

    GenreUpdateCommand(long id, @NonNull @NotBlank String name) {
        this.id = id
        this.name = name
    }
}
