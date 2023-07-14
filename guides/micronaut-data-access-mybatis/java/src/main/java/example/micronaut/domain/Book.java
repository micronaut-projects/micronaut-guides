package example.micronaut.domain;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.NotBlank;

@Serdeable
public class Book {

    @Nullable
    private Long id;

    @NonNull
    @NotBlank
    private String name;

    @NonNull
    @NotBlank
    private String isbn;

    private Genre genre;

    public Book(@NonNull @NotBlank String isbn,
                @NonNull @NotBlank String name,
                Genre genre) {
        this.isbn = isbn;
        this.name = name;
        this.genre = genre;
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
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(@NonNull String isbn) {
        this.isbn = isbn;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isbn='" + isbn + '\'' +
                ", genre=" + genre +
                '}';
    }
}
