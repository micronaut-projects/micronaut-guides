package example.micronaut.genre

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotBlank

@CompileStatic
@Serdeable
class GenreSaveCommand {

    @NotBlank
    @NonNull
    String name

    GenreSaveCommand(@NonNull @NotBlank String name) {
        this.name = name
    }
}
