//tag::packageandimports[]
package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Recoverable
import reactor.core.publisher.Mono

import jakarta.validation.constraints.NotBlank

import static io.micronaut.http.MediaType.TEXT_PLAIN
//end::packageandimports[]

/*
//tag::harcoded[]
@Client("http://localhost:8082") // <1>
@Recoverable(api = BookInventoryOperations)
//end::harcoded[]
*/
//tag::consul[]
@Client(id = "bookinventory") // <1>
@Recoverable(api = BookInventoryOperations)
//end::consul[]
//tag::clazz[]
interface BookInventoryClient extends BookInventoryOperations {

    @Consumes(TEXT_PLAIN)
    @Get("/books/stock/{isbn}")
    Mono<Boolean> stock(@NonNull @NotBlank String isbn)
}
//end::clazz[]
