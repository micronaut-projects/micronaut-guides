package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import jakarta.validation.constraints.NotBlank;

@Introspected
public class BookCatalogue {

    @NonNull
    @NotBlank
    private final String isbn;

    @NonNull
    @NotBlank
    private final String name;

    public BookCatalogue(@NonNull String isbn,
                         @NonNull String name) {
        this.isbn = isbn;
        this.name = name;
    }

    @NonNull
    public String getIsbn() {
        return isbn;
    }

    @NonNull
    public String getName() {
        return name;
    }
}
