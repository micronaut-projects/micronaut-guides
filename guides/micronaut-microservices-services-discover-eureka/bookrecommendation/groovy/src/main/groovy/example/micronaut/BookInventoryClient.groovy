package example.micronaut
/*
//tag::package[]
package example.micronaut
//end::package[]
*/
//tag::imports[]

import io.micronaut.core.annotation.NonNull
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Recoverable
import reactor.core.publisher.Mono

import jakarta.validation.constraints.NotBlank

import static io.micronaut.http.MediaType.TEXT_PLAIN
//end::imports[]

/*
//tag::harcoded[]
@Client("http://localhost:8082") // <1>
@Recoverable(api = BookInventoryOperations)
//end::harcoded[]
*/
//tag::eureka[]
@Client(id = "bookinventory") // <1>
@Recoverable(api = BookInventoryOperations)
//end::eureka[]
//tag::clazz[]
interface BookInventoryClient extends BookInventoryOperations {

    @Consumes(TEXT_PLAIN)
    @Get("/books/stock/{isbn}")
    Mono<Boolean> stock(@NonNull @NotBlank String isbn)
}
//end::clazz[]
