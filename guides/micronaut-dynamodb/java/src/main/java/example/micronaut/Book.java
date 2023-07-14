package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.NotBlank;

@Serdeable // <1>
public class Book implements Identified {

    @NonNull
    @NotBlank // <2>
    private final String id;

    @NonNull
    @NotBlank // <2>
    private final String isbn;

    @NonNull
    @NotBlank // <2>
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
