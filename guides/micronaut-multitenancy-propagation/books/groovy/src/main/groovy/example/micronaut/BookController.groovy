package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import groovy.transform.CompileStatic

@CompileStatic
@Controller("/books") // <1>
class BookController {

    private final BookService bookService

    BookController(BookService bookService) { // <2>
        this.bookService = bookService
    }

    @Get
    List<BookResponse> index() {
        bookService.findAll().collect { new BookResponse(title: it.title) }
    }
}
