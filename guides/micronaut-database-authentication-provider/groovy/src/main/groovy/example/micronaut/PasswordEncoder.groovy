package example.micronaut

import edu.umd.cs.findbugs.annotations.NonNull

import javax.validation.constraints.NotBlank

interface PasswordEncoder {
    String encode(@NotBlank @NonNull String rawPassword)

    boolean matches(@NotBlank @NonNull String rawPassword, @NotBlank @NonNull String encodedPassword)
}
