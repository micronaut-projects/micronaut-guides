package example.micronaut

import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import java.util.UUID
import javax.validation.Valid

@Controller
open class BookController {

    @Post
    open fun save(@Valid @Body book: Book): BookSaved {
        val bookSaved = BookSaved()
        bookSaved.name = book.name
        bookSaved.isbn = UUID.randomUUID().toString()
        return bookSaved
    }
}
