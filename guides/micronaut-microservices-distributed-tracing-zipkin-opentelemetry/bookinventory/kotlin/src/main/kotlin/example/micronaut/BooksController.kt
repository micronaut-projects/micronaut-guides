package example.micronaut

import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.tracing.annotation.ContinueSpan
import io.micronaut.tracing.annotation.SpanTag
import java.util.Optional
import jakarta.validation.constraints.NotBlank

@Controller("/books")
open class BooksController {

    @Produces(MediaType.TEXT_PLAIN)
    @Get("/stock/{isbn}")
    @ContinueSpan // <1>
    open fun stock(@SpanTag("stock.isbn") @NotBlank isbn: String): Boolean? {  // <2>
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
