package io.micronaut.guides.core;

import io.micronaut.context.BeanContext;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import io.micronaut.core.beans.BeanIntrospection;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class AppTest {

    @Inject
    Validator validator;

    @Inject
    BeanContext beanContext;

    @Test
    void typeAppPackageNameCanBeNull() {
        String packageName = null;
        Set<ConstraintViolation<App>> violations = validator.validate(new App(packageName));
        assertTrue(violations.isEmpty());
    }

    @Test
    void typeAppIsAnnotatedWithIntrospected() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(App.class));
    }

    @Test
    void typeAppIsDeserializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(App.class)));
    }

    @Test
    void typeAppIsNotSerializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(App.class)));
    }

}
