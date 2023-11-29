package example.micronaut;

import example.micronaut.controllers.LoginForm;
import io.micronaut.context.BeanContext;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@MicronautTest(startApplication = false)
class LoginFormTest {
    @Inject
    BeanContext beanContext;

    @Test
    void recordLoginFormIsAnnotatedWithSerdeableDeserializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(LoginForm.class)));
    }

    @Test
    void recordLoginFormIsAnnotatedWithSerdeableSerializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(LoginForm.class)));
    }

    @Test
    void isAnnotatedWithIntrospected() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(LoginForm.class));
    }

    @Test
    void validInstanceDoesNotHaveAnyConstraintViolation(Validator validator) {
        LoginForm valid = new LoginForm("admin", "admin123");
        assertTrue(validator.validate(valid).isEmpty());

        LoginForm invalid = new LoginForm("admin","");
        assertFalse(validator.validate(invalid).isEmpty());

        invalid = new LoginForm("admin",null);
        assertFalse(validator.validate(invalid).isEmpty());

        invalid = new LoginForm("", "admin123");
        assertFalse(validator.validate(invalid).isEmpty());

        invalid = new LoginForm(null, "admin123");
        assertFalse(validator.validate(invalid).isEmpty());
    }
}
