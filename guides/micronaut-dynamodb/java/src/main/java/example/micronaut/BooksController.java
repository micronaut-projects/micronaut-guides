package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@ExecuteOn(TaskExecutors.IO) // <1>
@Controller("/books") // <2>
public class BooksController {

    private final BookRepository bookRepository;

    public BooksController(BookRepository bookRepository) { // <3>
        this.bookRepository = bookRepository;
    }

    @Get // <4>
    public List<Book> index() {
        return bookRepository.findAll();
    }

    @Post // <5>
    public HttpResponse<?> save(@Body("isbn") @NonNull @NotBlank String isbn, // <6>
                             @Body("name") @NonNull @NotBlank String name) {
        String id = bookRepository.save(isbn, name);
        return HttpResponse.created(UriBuilder.of("/books").path(id).build());
    }

    @Get("/{id}") // <7>
    public Optional<Book> show(@PathVariable @NonNull @NotBlank String id) { // <8>
        return bookRepository.findById(id);
    }

    @Delete("/{id}") // <9>
    @Status(HttpStatus.NO_CONTENT) // <10>
    public void delete(@PathVariable @NonNull @NotBlank String id) {
        bookRepository.delete(id);
    }
}
