package example.micronaut

import io.micronaut.core.annotation.NonNull

import jakarta.validation.constraints.NotBlank

interface PasswordEncoder {
    String encode(@NotBlank @NonNull String rawPassword)

    boolean matches(@NotBlank @NonNull String rawPassword,
                    @NotBlank @NonNull String encodedPassword)
}
