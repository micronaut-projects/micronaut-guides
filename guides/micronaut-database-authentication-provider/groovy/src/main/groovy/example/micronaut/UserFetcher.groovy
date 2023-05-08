package example.micronaut

import io.micronaut.core.annotation.NonNull

import jakarta.validation.constraints.NotBlank

interface UserFetcher {
    UserState findByUsername(@NotBlank @NonNull String username)
}
