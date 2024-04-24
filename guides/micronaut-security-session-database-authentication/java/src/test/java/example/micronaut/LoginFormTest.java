/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
