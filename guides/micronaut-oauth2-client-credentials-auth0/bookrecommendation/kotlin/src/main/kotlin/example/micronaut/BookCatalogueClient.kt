package example.micronaut

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import io.micronaut.retry.annotation.Recoverable
import org.reactivestreams.Publisher

@Client(id = "bookcatalogue")
@Recoverable(api = BookCatalogueOperations::class)
interface BookCatalogueClient : BookCatalogueOperations {

    @Get("/books")
    override fun findAll(): Publisher<Book>
}
