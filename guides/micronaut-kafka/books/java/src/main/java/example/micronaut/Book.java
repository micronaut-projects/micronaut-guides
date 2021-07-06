package example.micronaut;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;

import java.util.Objects;

@Introspected
public class Book {

    private final String isbn;
    private final String name;

    @Creator
    public Book(String isbn, String name) {
        this.isbn = isbn;
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book other = (Book) o;
        return Objects.equals(isbn, other.isbn) &&
                Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn, name);
    }
}
