package example.micronaut

import edu.umd.cs.findbugs.annotations.NonNull
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.inject.Singleton
import javax.validation.constraints.NotBlank

@Singleton // <1>
class BCryptPasswordEncoderService implements PasswordEncoder {

    org.springframework.security.crypto.password.PasswordEncoder delegate = new BCryptPasswordEncoder()

    String encode(@NotBlank @NonNull String rawPassword) {
        delegate.encode(rawPassword)
    }

    @Override
    boolean matches(@NotBlank @NonNull String rawPassword, @NotBlank @NonNull String encodedPassword) {
        delegate.matches(rawPassword, encodedPassword)
    }
}
