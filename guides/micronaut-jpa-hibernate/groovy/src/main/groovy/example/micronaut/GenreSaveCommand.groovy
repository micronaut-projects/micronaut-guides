package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotBlank

@CompileStatic
@Serdeable // <1>
class GenreSaveCommand {

    @NotBlank
    String name

    GenreSaveCommand(String name) {
        this.name = name
    }
}
