package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotBlank

@CompileStatic
@Serdeable
class LinkedInMe {

    @NonNull
    @NotBlank
    final String id

    @NonNull
    @NotBlank
    final String localizedFirstName

    @NonNull
    @NotBlank
    final String localizedLastName

    LinkedInMe(@NonNull String id,
               @NonNull String localizedFirstName,
               @NonNull String localizedLastName) {
        this.id = id
        this.localizedFirstName = localizedFirstName
        this.localizedLastName = localizedLastName
    }
}
