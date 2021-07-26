//tag::packageandimports[]
package example.micronaut

import org.reactivestreams.Publisher
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Recoverable
//end::packageandimports[]

/*
//tag::harcoded[]
@Client("http://localhost:8081") // <1>
@Recoverable(api = BookCatalogueOperations.class)
//end::harcoded[]
*/
//tag::consul[]
@Client(id = "bookcatalogue") // <1>
@Recoverable(api = BookCatalogueOperations.class)
//end::consul[]
//tag::clazz[]
interface BookCatalogueClient extends BookCatalogueOperations {

    @Get("/books")
    Publisher<Book> findAll()
}
//end::clazz[]
