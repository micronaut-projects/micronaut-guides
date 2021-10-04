package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.List;
import java.util.Optional;

@Controller("/books") // <1>
class BookController {

    private final BookService bookService;

    BookController(BookService bookService) { // <2>
        this.bookService = bookService;
    }

    @Get // <3>
    List<Book> listAll() {
        return bookService.listAll();
    }

    @Get("/{isbn}") // <4>
    Optional<Book> findBook(String isbn) {
        return bookService.findByIsbn(isbn);
    }
}
