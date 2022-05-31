package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post

import javax.validation.Valid

@CompileStatic
@Controller
class BookController {

    @Post
    BookSaved save(@Valid @Body Book book) {
        BookSaved bookSaved = new BookSaved()
        bookSaved.name = book.name
        bookSaved.isbn = UUID.randomUUID().toString()
        bookSaved
    }
}
