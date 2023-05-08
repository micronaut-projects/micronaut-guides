package example.micronaut

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import io.micronaut.core.annotation.NonNull
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotBlank

@CompileStatic
@EqualsAndHashCode
@Serdeable
class BookRecommendation {

    @NonNull
    @NotBlank
    final String name

    BookRecommendation(@NonNull @NotBlank String name) {
        this.name = name
    }
}
