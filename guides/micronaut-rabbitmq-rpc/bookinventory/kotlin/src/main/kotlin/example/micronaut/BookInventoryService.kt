package example.micronaut

import io.micronaut.rabbitmq.annotation.Queue
import io.micronaut.rabbitmq.annotation.RabbitListener
import java.util.Optional
import jakarta.validation.constraints.NotBlank

@RabbitListener // <1>
open class BookInventoryService {

    @Queue("inventory") // <2>
    open fun stock(isbn: @NotBlank String?): Boolean? =
        bookInventoryByIsbn(isbn).map { (_, stock): BookInventory -> stock > 0 }.orElse(null)

    private fun bookInventoryByIsbn(isbn: String?): Optional<BookInventory> =
        if (isbn.equals("1491950358")) {
            Optional.of(BookInventory(isbn!!, 4))
        } else if (isbn.equals("1680502395")) {
            Optional.of(BookInventory(isbn!!, 0))
        } else {
            Optional.empty<Any>() as Optional<BookInventory>
        }
}
