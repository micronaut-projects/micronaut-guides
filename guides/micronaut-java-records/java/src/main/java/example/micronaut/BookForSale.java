package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Introspected;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Introspected // <1>
public record BookForSale(@NonNull @NotBlank String isbn,  // <2>
                          @NonNull @NotBlank String title,
                          @NonNull @NotNull BigDecimal price) { }
