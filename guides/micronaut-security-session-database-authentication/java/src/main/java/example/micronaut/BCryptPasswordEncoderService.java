package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;

@Singleton // <1>
class BCryptPasswordEncoderService implements PasswordEncoder {

    org.springframework.security.crypto.password.PasswordEncoder delegate = new BCryptPasswordEncoder();

    public String encode(@NotBlank @NonNull String rawPassword) {
        return delegate.encode(rawPassword);
    }

    @Override
    public boolean matches(@NotBlank @NonNull String rawPassword,
                           @NotBlank @NonNull String encodedPassword) {
        return delegate.matches(rawPassword, encodedPassword);
    }
}
