package example.micronaut.constraints;

import example.micronaut.controllers.SignUpForm;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class PasswordMatchValidatorTest {
    @Test
    void isAnnotatedWithIntrospected() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(PasswordMatchValidator.class));
    }

    @Test
    void passwordMatching() {
        PasswordMatchValidator validator = new PasswordMatchValidator();
        assertTrue(validator.isValid(new SignUpForm("admin", "admin123", "admin123"), null));
        assertFalse(validator.isValid(new SignUpForm("admin", "admin123", "foobar"), null));
        assertTrue(validator.isValid(new SignUpForm("admin", null, null), null));
        assertFalse(validator.isValid(new SignUpForm("admin", "admin123", null), null));
        assertFalse(validator.isValid(new SignUpForm("admin",  null, "admin123"), null));
    }
}