package example.micronaut

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor
import io.micronaut.core.annotation.Introspected

@CompileStatic
@TupleConstructor
@EqualsAndHashCode
@Introspected
class BookInventory {
    String isbn
    Integer stock
}
