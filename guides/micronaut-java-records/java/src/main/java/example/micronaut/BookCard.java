package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Serdeable // <1>
public record BookCard(@NonNull @NotBlank @Size(max = 255) String isbn, // <2>
                       @NonNull @NotBlank @Size(max = 255) String title, // <2>
                       @NonNull @NotNull BigDecimal price) {
}
