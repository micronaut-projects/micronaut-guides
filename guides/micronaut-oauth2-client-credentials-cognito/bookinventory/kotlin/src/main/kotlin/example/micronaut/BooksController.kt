package example.micronaut

import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import java.util.Optional
import jakarta.validation.constraints.NotBlank
import io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED
import io.micronaut.security.annotation.Secured

@Controller("/books")
open class BooksController {

    @Produces(TEXT_PLAIN)
    @Get("/stock/{isbn}")
    @Secured(IS_AUTHENTICATED) // <1>
    open fun stock(@NotBlank isbn: String): Boolean? =
        bookInventoryByIsbn(isbn).map { (_, stock) -> stock > 0 }.orElse(null)

    private fun bookInventoryByIsbn(isbn: String): Optional<BookInventory> {
        if (isbn == "1491950358") {
            return Optional.of(BookInventory(isbn, 4))
        }
        if (isbn == "1680502395") {
            return Optional.of(BookInventory(isbn, 0))
        }
        return Optional.empty()
    }
}
