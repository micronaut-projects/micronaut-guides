package example.micronaut.genre

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull

import javax.validation.constraints.NotBlank

@CompileStatic
@Introspected
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
