package example.micronaut.controllers;

import io.micronaut.context.BeanContext;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class SignUpFormTest {
    @Inject
    BeanContext beanContext;

    @Test
    void recordSignUpFormIsAnnotatedWithSerdeableDeserializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(SignUpForm.class)));
    }

    @Test
    void recordSignUpFormIsAnnotatedWithSerdeableSerializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(SignUpForm.class)));
    }

    @Test
    void isAnnotatedWithIntrospected() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(SignUpForm.class));
    }

    @Test
    void validInstanceDoesNotHaveAnyConstraintViolation(Validator validator) {
        SignUpForm valid = new SignUpForm("admin", "admin123", "admin123");
        assertTrue(validator.validate(valid).isEmpty());

        SignUpForm invalid = new SignUpForm("admin", "admin123", "foobar");
        assertFalse(validator.validate(invalid).isEmpty());

        invalid = new SignUpForm("admin","", "admin123");
        assertFalse(validator.validate(invalid).isEmpty());

        invalid = new SignUpForm("admin",null, "admin123");
        assertFalse(validator.validate(invalid).isEmpty());

        invalid = new SignUpForm("", "admin123", "admin123");
        assertFalse(validator.validate(invalid).isEmpty());

        invalid = new SignUpForm(null, "admin123", "admin123");
        assertFalse(validator.validate(invalid).isEmpty());
    }
}