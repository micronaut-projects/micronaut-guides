package example.micronaut

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import io.micronaut.serde.annotation.Serdeable

@Serdeable
@CompileStatic
@EqualsAndHashCode
@ToString
class Book {

    final String isbn
    final String name

    Book(String isbn, String name) {
        this.isbn = isbn
        this.name = name
    }
}
