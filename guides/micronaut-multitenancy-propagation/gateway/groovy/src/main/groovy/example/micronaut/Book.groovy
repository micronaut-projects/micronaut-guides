package example.micronaut

import io.micronaut.core.annotation.Introspected

@Introspected
class Book {
    String title

    Book(String title) {
        this.title = title
    }
}
