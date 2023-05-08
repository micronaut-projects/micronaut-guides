package example.micronaut;

import example.micronaut.model.BookAvailability;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;

import jakarta.validation.constraints.NotNull;

@MappedEntity("book") // <1>
public class BookEntity {

    @Id // <2>
    @GeneratedValue(GeneratedValue.Type.AUTO)
    private Long id;

    @NonNull
    @NotNull
    private String name;

    @NonNull
    @NotNull
    private BookAvailability availability;

    @Nullable
    private String author;

    @Nullable
    @MappedProperty("ISBN")
    private String isbn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NonNull
    public BookAvailability getAvailability() {
        return availability;
    }

    public void setAvailability(@NonNull BookAvailability availability) {
        this.availability = availability;
    }

    @Nullable
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(@Nullable String isbn) {
        this.isbn = isbn;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @Nullable
    public String getAuthor() {
        return author;
    }

    public void setAuthor(@Nullable String author) {
        this.author = author;
    }
}
