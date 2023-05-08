//tag::packageandimports[]
package example.micronaut

import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Recoverable
import reactor.core.publisher.Mono
import jakarta.validation.constraints.NotBlank
//end::packageandimports[]

/*
//tag::harcoded[]
@Client("http://localhost:8082") // <1>
@Recoverable(api = BookInventoryOperations::class)
//end::harcoded[]
*/
//tag::consul[]
@Client(id = "bookinventory") // <1>
@Recoverable(api = BookInventoryOperations::class)
//end::consul[]
//tag::clazz[]
interface BookInventoryClient : BookInventoryOperations {

    @Consumes(TEXT_PLAIN)
    @Get("/books/stock/{isbn}")
    override fun stock(@NotBlank isbn: String): Mono<Boolean>
}
//end::clazz[]
