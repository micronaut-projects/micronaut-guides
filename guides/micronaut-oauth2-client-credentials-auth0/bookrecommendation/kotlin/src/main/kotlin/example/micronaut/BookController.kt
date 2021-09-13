package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS
import io.micronaut.security.annotation.Secured

@Controller("/books")
class BookController(
        private val bookCatalogueOperations: BookCatalogueOperations,
        private val bookInventoryOperations: BookInventoryOperations) {

    @Get
    @Secured(IS_ANONYMOUS) // <1>
    fun index(): Publisher<BookRecommendation> =

        Flux.from(bookCatalogueOperations.findAll())
                .flatMap { b ->
                    Flux.from(bookInventoryOperations.stock(b.isbn))
                            .filter { hasStock -> hasStock }
                            .map { b }
                }.map { (_, name) -> BookRecommendation(name) }
}
