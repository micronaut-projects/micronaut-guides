package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.tracing.annotation.ContinueSpan
import io.micronaut.tracing.annotation.SpanTag
import jakarta.validation.constraints.NotBlank

@CompileStatic
@Controller("/books")
class BooksController {

    @Produces(MediaType.TEXT_PLAIN)
    @Get("/stock/{isbn}")
    @ContinueSpan // <1>
    Boolean stock(@SpanTag("stock.isbn") @NotBlank String isbn) {  // <2>
        bookInventoryByIsbn(isbn).map { bi -> bi.getStock() > 0 }.orElse(null)
    }

    private Optional<BookInventory> bookInventoryByIsbn(String isbn) {
        if (isbn == "1491950358") {
            return Optional.of(new BookInventory(isbn, 4))

        } else if (isbn == "1680502395") {
            return Optional.of(new BookInventory(isbn, 0))
        }
        Optional.empty()
    }
}
