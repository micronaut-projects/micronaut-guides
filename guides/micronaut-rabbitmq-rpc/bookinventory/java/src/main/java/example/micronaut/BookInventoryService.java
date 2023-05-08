package example.micronaut;

import io.micronaut.rabbitmq.annotation.Queue;
import io.micronaut.rabbitmq.annotation.RabbitListener;

import jakarta.validation.constraints.NotBlank;
import java.util.Optional;

@RabbitListener // <1>
public class BookInventoryService {

    @Queue("inventory") // <2>
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
