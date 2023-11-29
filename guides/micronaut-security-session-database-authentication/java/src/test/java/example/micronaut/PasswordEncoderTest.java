package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class PasswordEncoderTest {

    @Test
    void passwordEncoder(PasswordEncoder passwordEncoder) {
        String rawPassword = "admin123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        assertNotEquals(encodedPassword, rawPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

}