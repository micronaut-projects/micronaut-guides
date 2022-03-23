package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

import javax.validation.constraints.NotBlank

@CompileStatic
@Introspected // <1>
class GenreSaveCommand {

    @NotBlank
    String name

    GenreSaveCommand(String name) {
        this.name = name
    }
}
