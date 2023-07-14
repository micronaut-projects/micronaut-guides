package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Recoverable;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import jakarta.validation.constraints.NotBlank;

@Client(id = "bookinventory")
@Recoverable(api = BookInventoryOperations.class)
public interface BookInventoryClient extends BookInventoryOperations {

    @Consumes(MediaType.TEXT_PLAIN)
    @Get("/books/stock/{isbn}")
    Mono<Boolean> stock(@NotBlank String isbn);
}
