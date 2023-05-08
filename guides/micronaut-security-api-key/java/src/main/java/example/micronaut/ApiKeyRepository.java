package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.Optional;

@FunctionalInterface // <1>
public interface ApiKeyRepository {
    @NonNull
    Optional<Principal> findByApiKey(@NonNull @NotBlank String apiKey);
}
