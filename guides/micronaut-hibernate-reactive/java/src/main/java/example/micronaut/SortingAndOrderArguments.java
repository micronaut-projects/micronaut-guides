package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Serdeable
public record SortingAndOrderArguments(
        @Nullable @PositiveOrZero Integer offset,
        @Nullable @Positive Integer max,
        @Nullable @Pattern(regexp = "id|name") String sort,
        @Nullable @Pattern(regexp = "asc|ASC|desc|DESC") String order
) {
}
