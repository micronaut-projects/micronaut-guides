package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import jakarta.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest(startApplication = false) // <1>
class GreetingServiceTest {

    @Inject
    GreetingService greetingService;

    @Test
    void regexValidationNonDigitsWork() {
        assertDoesNotThrow(() -> greetingService.greeting("foo"));
        Executable e = () -> greetingService.greeting("12foo");
        assertThrows(ConstraintViolationException.class, e);
    }
}