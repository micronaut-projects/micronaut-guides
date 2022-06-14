package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@CompileStatic
@Introspected // <1>
class GenreUpdateCommand {

    @NotNull
    final Long id

    @NotBlank
    final String name

    GenreUpdateCommand(Long id, String name) {
        this.id = id
        this.name = name
    }
}

