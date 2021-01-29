package example.micronaut

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import java.util.*
import javax.validation.constraints.NotBlank

@Controller("/books") // <1>
open class BooksController {

    @Produces(MediaType.TEXT_PLAIN) // <2>
    @Get("/stock/{isbn}") // <3>
    open fun stock(@NotBlank isbn: String): Boolean? {
        return bookInventoryByIsbn(isbn).map { (_, stock) -> stock > 0 }.orElse(null)
    }

    private fun bookInventoryByIsbn(isbn: String): Optional<BookInventory> {
        if (isbn == "1491950358") {
            return Optional.of(BookInventory(isbn, 4))

        } else if (isbn == "1680502395") {
            return Optional.of(BookInventory(isbn, 0))
        }
        return Optional.empty()
    }
}
