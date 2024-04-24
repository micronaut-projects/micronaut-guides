package example.micronaut;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import java.util.List;

@Controller("/books") // <1>
class BookController {
    private final BookRepository bookRepository;

    BookController(BookRepository bookRepository) { // <2>
        this.bookRepository = bookRepository;
    }

    @ExecuteOn(TaskExecutors.BLOCKING) // <3>
    @Get // <4>
    List<Book> index() {
        return bookRepository.findAll();
    }

    @ExecuteOn(TaskExecutors.BLOCKING) // <3>
    @Post
    @Status(HttpStatus.CREATED)
    void index(@Body String title) {
        bookRepository.save(title);
    }
}
