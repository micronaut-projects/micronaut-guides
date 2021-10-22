package example.micronaut

import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Recoverable
import reactor.core.publisher.Mono
import javax.validation.constraints.NotBlank

@Client(id = "bookinventory")
@Recoverable(api = BookInventoryOperations::class)
interface BookInventoryClient : BookInventoryOperations {
    @Consumes(TEXT_PLAIN)
    @Get("/books/stock/{isbn}")
    override fun stock(@NotBlank isbn: String): Mono<Boolean>
}
