package example.micronaut;

import io.micronaut.context.MessageSource;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false) // <1>
class ContactTest {
    @Inject // <2>
    MessageSource messageSource;

    @Inject // <3>
    Validator validator;

    @Test
    void contactValidation() {
        assertTrue(validator.validate(new Contact("+14155552671")).isEmpty());
        Set<ConstraintViolation<Contact>> violationSet = validator.validate(new Contact("+1-4155552671"));
        assertFalse(violationSet.isEmpty());
        String template = "{example.micronaut.E164.message}";
        assertTrue(violationSet.stream().anyMatch(violation -> violation.getMessageTemplate().equals(template)));
        assertTrue(violationSet.stream().anyMatch(violation -> violation.getInvalidValue().equals("+1-4155552671")));

        violationSet.stream().filter(violation -> violation.getMessage().equals(template))
                .findFirst()
                .ifPresent(violation -> {
                    Optional<String> message = messageSource.getMessage(violation.getMessage(), Locale.ENGLISH);
                    assertTrue(message.isPresent());
                    assertEquals("must be a phone in E.164 format", message.get());
                });
    }
}
