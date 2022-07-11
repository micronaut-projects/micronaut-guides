package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

import javax.validation.constraints.NotBlank

@CompileStatic
@Introspected
class GenreUpdateCommand {

    long id

    @NotBlank
    String name

    GenreUpdateCommand(long id, String name) {
        this.id = id
        this.name = name
    }
}
