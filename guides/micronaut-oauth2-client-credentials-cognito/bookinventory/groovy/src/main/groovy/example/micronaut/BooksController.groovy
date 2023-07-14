package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import jakarta.validation.constraints.NotBlank
import static io.micronaut.http.MediaType.TEXT_PLAIN
import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;
import io.micronaut.security.annotation.Secured

@CompileStatic
@Controller("/books")
class BooksController {

    @Produces(TEXT_PLAIN)
    @Get("/stock/{isbn}")
    @Secured(IS_AUTHENTICATED) // <1>
    Boolean stock(@NotBlank String isbn) {
        bookInventoryByIsbn(isbn).map(bi -> bi.stock > 0).orElse(null)
    }

    private Optional<BookInventory> bookInventoryByIsbn(String isbn) {
        if (isbn == "1491950358") {
            return Optional.of(new BookInventory(isbn, 4))
        }
        if (isbn == "1680502395") {
            return Optional.of(new BookInventory(isbn, 0))
        }
        Optional.empty()
    }
}
