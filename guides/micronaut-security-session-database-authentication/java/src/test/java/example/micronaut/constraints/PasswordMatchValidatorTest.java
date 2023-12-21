/*
 * Copyright 2017-2023 original authors
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