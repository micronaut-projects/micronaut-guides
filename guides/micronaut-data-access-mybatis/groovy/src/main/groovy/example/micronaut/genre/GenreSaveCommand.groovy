package example.micronaut.genre

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull

import javax.validation.constraints.NotBlank

@CompileStatic
@Introspected
class GenreSaveCommand {

    @NotBlank
    @NonNull
    String name

    GenreSaveCommand(@NonNull @NotBlank String name) {
        this.name = name
    }
}
