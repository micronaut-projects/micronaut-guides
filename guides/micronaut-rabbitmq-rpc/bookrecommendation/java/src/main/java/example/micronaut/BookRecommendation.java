package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;

import java.util.Objects;

@Serdeable
public class BookRecommendation {

    private final String name;

    public BookRecommendation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookRecommendation that = (BookRecommendation) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
