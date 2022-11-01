package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

import javax.validation.constraints.NotBlank

@CompileStatic
@Serdeable
class GenreUpdateCommand {

    long id

    @NotBlank
    String name

    GenreUpdateCommand(long id, String name) {
        this.id = id
        this.name = name
    }
}
