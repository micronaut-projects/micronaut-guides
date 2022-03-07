package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable

/**
 * <a href="https://datatracker.ietf.org/doc/html/rfc7517">JSON Web Key</a>
 */
interface JsonWebKeyGenerator {

    @NonNull
    Optional<String> generateJsonWebKey(@Nullable String kid)
}
