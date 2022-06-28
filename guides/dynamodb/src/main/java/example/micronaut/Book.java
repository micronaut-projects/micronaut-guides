package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;

import javax.validation.constraints.NotBlank;

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

    public Book(@NonNull String id,
                @NonNull String isbn,
                @NonNull String name) {
        this.id = id;
        this.isbn = isbn;
        this.name = name;
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
}
