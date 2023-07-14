package example.micronaut;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@ConfigurationProperties("vat") // <1>
public record ValueAddedTaxConfiguration(
    @NonNull @NotNull BigDecimal percentage) {
}
