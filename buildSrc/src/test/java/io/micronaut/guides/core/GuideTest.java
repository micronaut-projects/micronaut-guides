package io.micronaut.guides.core;

import io.micronaut.context.BeanContext;
import io.micronaut.core.type.Argument;
import io.micronaut.serde.SerdeIntrospections;
import io.micronaut.starter.application.ApplicationType;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import io.micronaut.core.beans.BeanIntrospection;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class GuideTest {

    @Inject
    Validator validator;

    @Inject
    BeanContext beanContext;

    @Test
    void typeCloudCanBeNull() {
        Set<ConstraintViolation<Guide>> violations = validator.validate(
                new Guide(null,null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null,null,null,null));
        assertTrue(violations.isEmpty());
    }

    @Test
    void typeAppIsAnnotatedWithIntrospected() {
        assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(Guide.class));
    }

    @Test
    void typeGuideIsDeserializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getDeserializableIntrospection(Argument.of(Guide.class)));
    }

    @Test
    void typeGuideIsNotSerializable() {
        SerdeIntrospections serdeIntrospections = beanContext.getBean(SerdeIntrospections.class);
        assertDoesNotThrow(() -> serdeIntrospections.getSerializableIntrospection(Argument.of(Guide.class)));
    }

}
