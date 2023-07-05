package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record Book(
        @NotBlank String id,
        @NotBlank String isbn,
        @NotBlank String name,
        @Nullable Integer stock
) implements Identified {

    @Override
    @NonNull
    public String getId() {
        return id;
    }
}
