package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import reactor.core.publisher.Flux
import org.reactivestreams.Publisher

@Controller("/books") // <1>
class BooksController(private val bookCatalogueOperations: BookCatalogueOperations,
                      private val bookInventoryOperations: BookInventoryOperations) { // <2>

    @Get // <3>
    fun index(): Publisher<BookRecommendation> {
        return Flux.from(bookCatalogueOperations.findAll())
                .flatMap { b ->
                    Flux.from(bookInventoryOperations.stock(b.isbn))
                            .filter { hasStock -> hasStock }
                            .map { _ -> b }
                }.map { (_, name) -> BookRecommendation(name) }
    }
}
