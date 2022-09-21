package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

@CompileStatic
@Introspected // <1>
class CommandBookSave {

    @NotBlank // <2>
    String title

    @Positive // <3>
    int pages
}
