package example.micronaut;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Status;

@Controller("/books") // <1>
class BookController {
    private final BookRepository bookRepository;

    BookController(BookRepository bookRepository) { // <2>
        this.bookRepository = bookRepository;
    }

    @Post // <3>
    @Status(HttpStatus.CREATED) // <4>
    void save(@Body Book book) { // <5>
        bookRepository.upsert(book); // <6>
    }

    @Put // <7>
    @Status(HttpStatus.OK)
    void update(@Body Book book) {
        bookRepository.upsert(book); // <6>
    }
}