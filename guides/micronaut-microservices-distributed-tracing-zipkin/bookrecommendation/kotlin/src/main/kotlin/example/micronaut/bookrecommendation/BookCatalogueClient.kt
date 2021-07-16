//tag::packageandimports[]
package example.micronaut

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Recoverable
import reactor.core.publisher.Flux

//end::packageandimports[]
//tag::harcoded[]
@Client("http://localhost:8081") // <1>
@Recoverable(api = BookCatalogueOperations::class)
//end::harcoded[]
//tag::clazz[]
interface BookCatalogueClient : BookCatalogueOperations {

    @Get("/books")
    override fun findAll(): Publisher<Book>
}
//end::clazz[]
