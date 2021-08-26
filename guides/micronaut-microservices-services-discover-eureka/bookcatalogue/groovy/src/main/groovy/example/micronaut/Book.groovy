package example.micronaut

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import io.micronaut.core.annotation.NonNull

import javax.validation.constraints.NotBlank

@CompileStatic
@EqualsAndHashCode
@Introspected
class Book {

    @NonNull
    @NotBlank
    final String isbn

    @NonNull
    @NotBlank
    final String name

    @Creator
    Book(@NonNull @NotBlank String isbn,
         @NonNull @NotBlank String name) {
        this.isbn = isbn
        this.name = name
    }
}
