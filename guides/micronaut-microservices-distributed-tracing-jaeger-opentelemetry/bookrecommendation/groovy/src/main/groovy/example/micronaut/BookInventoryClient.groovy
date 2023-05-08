//tag::packageandimports[]
package example.micronaut

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Recoverable
import reactor.core.publisher.Mono
import jakarta.validation.constraints.NotBlank
//end::packageandimports[]

//tag::harcoded[]
@Client("http://localhost:8082") // <1>
@Recoverable(api = BookInventoryOperations)
//end::harcoded[]
//tag::clazz[]
interface BookInventoryClient extends BookInventoryOperations {

    @Consumes(MediaType.TEXT_PLAIN)
    @Get("/books/stock/{isbn}")
    Mono<Boolean> stock(@NotBlank String isbn)
}
//end::clazz[]
