package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import java.util.Optional;

/**
 * <a href="https://datatracker.ietf.org/doc/html/rfc7517">JSON Web Key</a>
 */
public interface JsonWebKeyGenerator {

    @NonNull
    Optional<String> generateJsonWebKey(@Nullable String kid);
}
