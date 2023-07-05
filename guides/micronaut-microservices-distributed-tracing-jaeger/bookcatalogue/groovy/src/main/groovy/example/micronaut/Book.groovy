package example.micronaut

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor
import io.micronaut.serde.annotation.Serdeable

@TupleConstructor
@EqualsAndHashCode
@Serdeable
@CompileStatic
class Book {
    String isbn
    String name
}
