package example.micronaut

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.core.annotation.ReflectiveAccess
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@Canonical
@CompileStatic
@Serdeable // <1>
@ReflectiveAccess // <2>
class Contact {

    @NonNull
    @NotBlank // <3>
    String firstName

    @NonNull
    @NotBlank
    String lastName

    @Nullable // <4>
    String company
}
