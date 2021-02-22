package example.micronaut;

import io.micronaut.core.annotation.Introspected;

import java.util.Objects;

@Introspected
public class BookInventory {
    private String isbn;
    private Integer stock;

    public BookInventory() {
    }

    public BookInventory(String isbn, Integer stock) {
        this.isbn = isbn;
        this.stock = stock;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
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
