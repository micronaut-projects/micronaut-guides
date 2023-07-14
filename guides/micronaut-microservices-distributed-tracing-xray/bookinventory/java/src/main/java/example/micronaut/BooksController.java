package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

import jakarta.validation.constraints.NotBlank;

@Controller("/books")
public class BooksController {

    private final BookRepository bookRepository;

    public BooksController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Produces(MediaType.TEXT_PLAIN)
    @Get("/stock/{isbn}")
    public Boolean stock(@NotBlank String isbn) { // <2>
        return bookRepository.findByIsbn(isbn)
                .map(Book::stock)
                .map(i -> i > 0)
                .orElse(null);
    }
}
