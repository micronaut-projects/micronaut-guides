//tag::packageandimports[]
package example.micronaut

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Recoverable
import reactor.core.publisher.Flux
import org.reactivestreams.Publisher

//end::packageandimports[]
/*
//tag::harcoded[]
@Client("http://localhost:8081") // <1>
@Recoverable(api = BookCatalogueOperations::class)
//end::harcoded[]
*/
//tag::consul[]
@Client(id = "bookcatalogue") // <1>
@Recoverable(api = BookCatalogueOperations::class)
//end::consul[]
//tag::clazz[]
interface BookCatalogueClient : BookCatalogueOperations {

    @Get("/books")
    override fun findAll(): Publisher<Book>
}
//end::clazz[]
