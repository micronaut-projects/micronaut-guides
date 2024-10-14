package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

@Introspected // <1>
public record ContactPreview(
        @NonNull Long id,
        @Nullable String firstName,
        @Nullable String lastName
) {
}
