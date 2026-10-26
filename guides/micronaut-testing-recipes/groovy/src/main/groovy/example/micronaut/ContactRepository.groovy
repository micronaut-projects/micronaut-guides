package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import jakarta.validation.constraints.NotBlank

@CompileStatic
interface ContactRepository {

    @NonNull
    List<Contact> findAll()

    @NonNull
    Optional<Contact> findByFirstName(@NonNull @NotBlank String firstName) // <1>
}
