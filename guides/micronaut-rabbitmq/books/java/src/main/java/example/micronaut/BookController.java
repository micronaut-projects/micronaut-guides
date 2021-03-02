package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.List;
import java.util.Optional;

@Controller("/books") // <1>
public class BookController {

    private final BookService bookService; // <2>

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Get // <3>
    public List<Book> listAll() {
        return bookService.listAll();
    }

    @Get("/{isbn}") // <4>
    Optional<Book> findBook(String isbn) {
        return bookService.findByIsbn(isbn);
    }
}
