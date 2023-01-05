package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@CompileStatic
@Serdeable // <1>
class GenreUpdateCommand {

    final long id

    @NotBlank
    final String name

    GenreUpdateCommand(Long id, String name) {
        this.id = id
        this.name = name
    }
}

