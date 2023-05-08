package example.micronaut

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import io.micronaut.core.annotation.NonNull
import io.micronaut.serde.annotation.Serdeable

import jakarta.validation.constraints.NotBlank

@CompileStatic
@EqualsAndHashCode
@Serdeable
class BookInventory {

    @NonNull
    @NotBlank
    final String isbn

    final int stock

    BookInventory(@NonNull @NotBlank String isbn,
                  int stock) {
        this.isbn = isbn
        this.stock = stock
    }
}
