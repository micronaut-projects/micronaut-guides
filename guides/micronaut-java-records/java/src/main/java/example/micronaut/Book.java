package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@MappedEntity // <1>
public record Book(@Id @NonNull @NotBlank @Size(max = 255) String isbn, // <2>
                   @NonNull @NotBlank @Size(max = 255) String title, // <3>
                   @NonNull @NotNull BigDecimal price, // <3>
                   @Nullable String about) {  // <4>
}
