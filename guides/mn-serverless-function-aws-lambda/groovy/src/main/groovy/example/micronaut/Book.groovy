package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull

import javax.validation.constraints.NotBlank

@CompileStatic
@Introspected
class Book {

    @NonNull
    @NotBlank
    String name
}
