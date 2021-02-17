package example.micronaut

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor
import io.micronaut.core.annotation.Introspected

@TupleConstructor
@EqualsAndHashCode
@Introspected
@CompileStatic
class Book {
    String isbn
    String name
}
