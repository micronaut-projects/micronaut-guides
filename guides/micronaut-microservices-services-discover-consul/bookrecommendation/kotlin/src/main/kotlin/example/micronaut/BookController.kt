package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

@Controller("/books") // <1>
class BookController(
        private val bookCatalogueOperations: BookCatalogueOperations,
        private val bookInventoryOperations: BookInventoryOperations) { // <2>

    @Get // <3>
    fun index(): Publisher<BookRecommendation> =

        Flux.from(bookCatalogueOperations.findAll())
                .flatMap { b ->
                    Flux.from(bookInventoryOperations.stock(b.isbn))
                            .filter { hasStock -> hasStock }
                            .map { b }
                }.map { (_, name) -> BookRecommendation(name) }
}
