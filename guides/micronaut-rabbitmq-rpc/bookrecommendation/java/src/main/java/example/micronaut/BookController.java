package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.reactivex.Flowable;

@Controller("/books") // <1>
public class BookController {

    private final CatalogueClient catalogueClient; // <2>
    private final InventoryClient inventoryClient; // <2>

    public BookController(CatalogueClient catalogueClient, InventoryClient inventoryClient) { // <2>
        this.catalogueClient = catalogueClient;
        this.inventoryClient = inventoryClient;
    }

    @Get("/") // <3>
    public Flowable<BookRecommendation> index() {
        return catalogueClient.findAll(null)
                .flatMap(Flowable::fromIterable)
                .flatMapMaybe(book -> inventoryClient.stock(book.getIsbn())
                        .filter(Boolean::booleanValue)
                        .map(response -> book))
                .map(book -> new BookRecommendation(book.getName()));
    }

}
