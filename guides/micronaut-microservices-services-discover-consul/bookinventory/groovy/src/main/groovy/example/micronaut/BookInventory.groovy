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
class BookInventory {

    @NonNull
    @NotBlank
    final String isbn

    final int stock

    @Creator
    BookInventory(@NonNull @NotBlank String isbn,
                  int stock) {
        this.isbn = isbn
        this.stock = stock
    }
}
