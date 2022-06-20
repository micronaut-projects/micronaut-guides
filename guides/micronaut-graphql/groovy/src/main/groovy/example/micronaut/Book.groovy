package example.micronaut

import io.micronaut.core.annotation.Introspected

@Introspected
class Book {

    final String id
    final String name
    final int pageCount
    final Author author

    Book(String id, String name, int pageCount, Author author) {
        this.id = id
        this.name = name
        this.pageCount = pageCount
        this.author = author
    }

}
