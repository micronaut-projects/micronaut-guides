package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.annotation.Counted;

import java.util.Optional;

@Controller("/books") // <1>
@ExecuteOn(TaskExecutors.IO) // <2>
class BookController {

    private final BookRepository bookRepository;

    BookController(BookRepository bookRepository) { // <3>
        this.bookRepository = bookRepository;
    }

    @Get// <4>
    @Timed("books.index") // <5>
    Iterable<Book> index() {
        return bookRepository.findAll();
    }

    @Get("/{isbn}") // <6>
    @Counted("books.find") // <7>
    Optional<Book> findBook(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
}
