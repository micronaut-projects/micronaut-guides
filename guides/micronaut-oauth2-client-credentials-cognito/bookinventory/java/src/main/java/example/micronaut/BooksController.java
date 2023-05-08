package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import jakarta.validation.constraints.NotBlank;
import java.util.Optional;
import static io.micronaut.http.MediaType.TEXT_PLAIN;
import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;
import io.micronaut.security.annotation.Secured;

@Controller("/books")
public class BooksController {

    @Produces(TEXT_PLAIN)
    @Get("/stock/{isbn}")
    @Secured(IS_AUTHENTICATED) // <1>
    public Boolean stock(@NotBlank String isbn) {
        return bookInventoryByIsbn(isbn).map(bi -> bi.getStock() > 0).orElse(null);
    }

    private Optional<BookInventory> bookInventoryByIsbn(String isbn) {
        if (isbn.equals("1491950358")) {
            return Optional.of(new BookInventory(isbn, 4));
        }
        if (isbn.equals("1680502395")) {
            return Optional.of(new BookInventory(isbn, 0));
        }
        return Optional.empty();
    }
}
