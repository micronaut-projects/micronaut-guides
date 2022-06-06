package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.util.List;
import java.util.Optional;

@Controller("/books") // <1>
@ExecuteOn(TaskExecutors.IO) // <2>
class BookController {

    private final BookRepository bookRepository;

    BookController(BookRepository bookRepository) { // <3>
        this.bookRepository = bookRepository;
    }

    @Get// <4>
    List<Book> index() {
        return bookRepository.findAll();
    }

    @Get("/{isbn}") // <5>
    Optional<Book> findBook(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }
}
