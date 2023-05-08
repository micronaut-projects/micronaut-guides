package example.micronaut;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import jakarta.validation.constraints.NotBlank;

@Introspected
public class Book implements Identified {

    @NonNull
    @NotBlank
    private final String id;

    @NonNull
    @NotBlank
    private final String isbn;

    @NonNull
    @NotBlank
    private final String name;

    @Nullable
    private final Integer stock;

    public Book(@NonNull String id,
                @NonNull String isbn,
                @NonNull String name) {
        this(id, isbn, name, null);
    }

    @Creator
    public Book(@NonNull String id,
                @NonNull String isbn,
                @NonNull String name,
                @Nullable Integer stock) {
        this.id = id;
        this.isbn = isbn;
        this.name = name;
        this.stock = stock;
    }

    @Override
    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getIsbn() {
        return isbn;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Nullable
    public Integer getStock() {
        return stock;
    }
}
