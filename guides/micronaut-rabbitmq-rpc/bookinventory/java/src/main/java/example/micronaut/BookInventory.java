package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;

@Serdeable
public class BookInventory {

    private final String isbn;
    private final Integer stock;

    public BookInventory(String isbn, Integer stock) {
        this.isbn = isbn;
        this.stock = stock;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getStock() {
        return stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookInventory that = (BookInventory) o;
        return Objects.equals(isbn, that.isbn) &&
                Objects.equals(stock, that.stock);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, stock);
    }
}
