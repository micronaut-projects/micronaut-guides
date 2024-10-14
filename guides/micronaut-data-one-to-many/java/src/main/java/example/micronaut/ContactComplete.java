package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import java.util.Set;

@Introspected
public record ContactComplete(
        @NonNull Long id,
        @Nullable String firstName,
        @Nullable String lastName,
        @Nullable Set<String> phones) {
}
