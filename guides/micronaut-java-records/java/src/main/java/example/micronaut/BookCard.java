package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Serdeable // <1>
public record BookCard(@NonNull @NotBlank @Size(max = 255) String isbn, // <2>
                       @NonNull @NotBlank @Size(max = 255) String title, // <2>
                       @NonNull @NotNull BigDecimal price) {
}
