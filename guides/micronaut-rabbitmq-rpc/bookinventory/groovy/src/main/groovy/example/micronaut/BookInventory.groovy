package example.micronaut

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected

@Introspected
@CompileStatic
@EqualsAndHashCode
class BookInventory {

    final String isbn
    final Integer stock

    @Creator
    BookInventory(String isbn, Integer stock) {
        this.isbn = isbn
        this.stock = stock
    }
}
