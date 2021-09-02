package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Introspected
public class BookInventory {

    @NonNull
    @NotBlank
    private final String isbn;

    private final int stock;

    public BookInventory(@NonNull @NotBlank String isbn,
                         int stock) {
        this.isbn = isbn;
        this.stock = stock;
    }

    @NonNull
    public String getIsbn() {
        return isbn;
    }

    public int getStock() {
        return stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookInventory that = (BookInventory) o;
        return stock == that.stock && Objects.equals(isbn, that.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, stock);
    }
}
