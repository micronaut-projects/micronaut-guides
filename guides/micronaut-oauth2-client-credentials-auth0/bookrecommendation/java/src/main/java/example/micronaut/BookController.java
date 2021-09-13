package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import static io.micronaut.security.rules.SecurityRule.IS_ANONYMOUS;
import io.micronaut.security.annotation.Secured;

@Controller("/books")
public class BookController {

    private final BookCatalogueOperations bookCatalogueOperations;
    private final BookInventoryOperations bookInventoryOperations;

    public BookController(BookCatalogueOperations bookCatalogueOperations,
                          BookInventoryOperations bookInventoryOperations) { 
        this.bookCatalogueOperations = bookCatalogueOperations;
        this.bookInventoryOperations = bookInventoryOperations;
    }

    @Get
    @Secured(IS_ANONYMOUS)
    public Publisher<BookRecommendation> index() {
        return Flux.from(bookCatalogueOperations.findAll())
                .flatMap(b -> Flux.from(bookInventoryOperations.stock(b.getIsbn()))
                        .filter(Boolean::booleanValue)
                        .map(rsp -> b)
                ).map(book -> new BookRecommendation(book.getName()));
    }
}
