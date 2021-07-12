package example.micronaut

import io.micronaut.core.annotation.NonNull

import javax.validation.constraints.NotBlank

interface UserFetcher {
    UserState findByUsername(@NotBlank @NonNull String username)
}
