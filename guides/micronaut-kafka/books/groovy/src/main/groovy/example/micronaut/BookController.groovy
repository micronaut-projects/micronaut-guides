package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@CompileStatic
@Controller('/books')
class BookController {

    private final BookService bookService

    BookController(BookService bookService) {
        this.bookService = bookService
    }

    @Get
    List<Book> listAll() {
        return bookService.listAll()
    }

    @Get('/{isbn}')
    Optional<Book> findBook(String isbn) {
        return bookService.findByIsbn(isbn)
    }
}
