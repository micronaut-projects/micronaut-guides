package example.micronaut

import io.micronaut.core.annotation.Introspected
import io.micronaut.function.aws.MicronautRequestHandler

@Introspected
class BookRequestHandler extends MicronautRequestHandler<Book, BookSaved> {

    @Override
    BookSaved execute(Book input) {
        BookSaved bookSaved = new BookSaved()
        bookSaved.with {
            name = input.getName()
            isbn = UUID.randomUUID().toString()
        }
        bookSaved
    }
}
