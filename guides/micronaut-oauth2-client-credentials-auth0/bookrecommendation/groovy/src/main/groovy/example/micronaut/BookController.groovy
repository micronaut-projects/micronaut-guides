package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS
import io.micronaut.security.annotation.Secured

@CompileStatic
@Controller("/books")
class BookController {

    private final BookCatalogueOperations bookCatalogueOperations
    private final BookInventoryOperations bookInventoryOperations

    BookController(BookCatalogueOperations bookCatalogueOperations,
                   BookInventoryOperations bookInventoryOperations) {
        this.bookCatalogueOperations = bookCatalogueOperations
        this.bookInventoryOperations = bookInventoryOperations
    }

    @Get
    @Secured(IS_ANONYMOUS) // <1>
    Publisher<BookRecommendation> index() {
        Flux.from(bookCatalogueOperations.findAll())
                .flatMap(b -> Flux.from(bookInventoryOperations.stock(b.isbn))
                        .filter(Boolean::booleanValue)
                        .map(rsp -> b)
                ).map(book -> new BookRecommendation(book.name))
    }
}
