package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

@Controller("/books") // <1>
class BookController(
        private val catalogueClient: CatalogueClient, // <2>
        private val inventoryClient: InventoryClient) {

    @Get // <3>
    fun index(): Publisher<BookRecommendation> =
        Flux.from(catalogueClient.findAll(null))
                .flatMap { Flux.fromIterable(it) }
                .flatMap { book: Book ->
                    Flux.from(inventoryClient.stock(book.isbn))
                            .filter { obj: Boolean? -> obj == true }
                            .map { book }
                }
                .map { (_, name): Book -> BookRecommendation(name) }
}
