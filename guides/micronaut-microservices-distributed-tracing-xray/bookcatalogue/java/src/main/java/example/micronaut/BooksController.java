package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import java.util.List;
import java.util.stream.Collectors;

@Controller("/books")
public class BooksController {

    private final BookRepository bookRepository;

    public BooksController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Get
    public List<BookCatalogue> index() {
        return bookRepository.findAll()
                .stream()
                .map(book -> new BookCatalogue(book.isbn(), book.name()))
                .collect(Collectors.toList());
    }
}
