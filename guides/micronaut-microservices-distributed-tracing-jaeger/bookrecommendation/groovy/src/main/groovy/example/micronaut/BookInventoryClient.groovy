//tag::packageandimports[]
package example.micronaut

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Recoverable
import org.reactivestreams.Publisher
import io.micronaut.core.async.annotation.SingleResult
import javax.validation.constraints.NotBlank
//end::packageandimports[]

//tag::harcoded[]
@Client("http://localhost:8082") // <1>
@Recoverable(api = BookInventoryOperations.class)
//end::harcoded[]
//tag::clazz[]
interface BookInventoryClient extends BookInventoryOperations {

    @Consumes(MediaType.TEXT_PLAIN)
    @Get("/books/stock/{isbn}")
    @SingleResult
    Publisher<Boolean> stock(@NotBlank String isbn)
}
//end::clazz[]
