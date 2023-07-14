package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces

import jakarta.validation.constraints.NotBlank

import static io.micronaut.http.MediaType.TEXT_PLAIN

@CompileStatic
@Controller("/books") // <1>
class BooksController {

    @Produces(TEXT_PLAIN) // <2>
    @Get("/stock/{isbn}") // <3>
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
