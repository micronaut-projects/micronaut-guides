package example.micronaut

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.TupleConstructor

@CompileStatic
@TupleConstructor
@EqualsAndHashCode
class Book {
    String isbn
    String name
}
