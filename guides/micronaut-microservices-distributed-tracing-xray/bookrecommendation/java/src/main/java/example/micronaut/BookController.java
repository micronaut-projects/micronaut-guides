package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import reactor.core.publisher.Flux;
import org.reactivestreams.Publisher;

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
    public Publisher<BookRecommendation> index() {
        return Flux.from(bookCatalogueOperations.findAll())
                .flatMap(b -> Flux.from(bookInventoryOperations.stock(b.isbn()))
                        .filter(Boolean::booleanValue)
                        .map(rsp -> b)
                ).map(book -> new BookRecommendation(book.name()));
    }
}
