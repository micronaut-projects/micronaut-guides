package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@MappedEntity // <1>
public record Book(/* @GeneratedValue */ @Id @Nullable Long id, // <2>
                   @NonNull @NotBlank @Size(max = 255) String title, // <3>
                   @NonNull @NotBlank @Size(max = 255) String isbn, // <3>
                   @NonNull @NotNull BigDecimal price, // <3>
                   @Nullable String about) {  // <4>
}
