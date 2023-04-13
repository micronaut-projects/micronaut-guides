package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.ReflectiveAccess;
import io.micronaut.serde.annotation.Serdeable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Serdeable // <1>
@ReflectiveAccess // <2>
public record BookForSale(@NonNull @NotBlank String isbn,  // <3>
                          @NonNull @NotBlank String title,
                          @NonNull @NotNull BigDecimal price) { }
