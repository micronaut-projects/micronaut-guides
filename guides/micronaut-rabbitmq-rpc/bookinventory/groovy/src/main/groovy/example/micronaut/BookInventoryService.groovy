package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.rabbitmq.annotation.Queue
import io.micronaut.rabbitmq.annotation.RabbitListener

import jakarta.validation.constraints.NotBlank

@CompileStatic
@RabbitListener // <1>
class BookInventoryService {

    @Queue('inventory') // <2>
    Boolean stock(@NotBlank String isbn) {
        bookInventoryByIsbn(isbn).map(bi -> bi.getStock() > 0).orElse(null)
    }

    private Optional<BookInventory> bookInventoryByIsbn(String isbn) {
        if (isbn == '1491950358') {
            return Optional.of(new BookInventory(isbn, 4))
        }
        if (isbn == '1680502395') {
            return Optional.of(new BookInventory(isbn, 0))
        }
        return Optional.empty()
    }
}
