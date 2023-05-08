package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

@CompileStatic
@Serdeable // <1>
class CommandBookSave {

    @NotBlank // <2>
    String title

    @Positive // <3>
    int pages
}
