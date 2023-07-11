package example.micronaut.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Serdeable
public class Genre {

    @Nullable
    private Long id;

    @NonNull
    @NotBlank
    private String name;

    @NonNull
    @JsonIgnore
    private Set<Book> books = new HashSet<>();

    public Genre(@NonNull @NotBlank String name) {
        this.name = name;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(@NonNull Set<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", books=" + books +
                '}';
    }
}
