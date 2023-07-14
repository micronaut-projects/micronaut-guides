package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.serde.annotation.Serdeable

import static io.micronaut.data.annotation.GeneratedValue.Type.AUTO

@CompileStatic
@MappedEntity // <1>
@Serdeable
class Book {

    @Id // <2>
    @GeneratedValue(AUTO) // <3>
    Long id

    String name
    String isbn

    Book(String isbn, String name) {
        this.isbn = isbn
        this.name = name
    }
}
