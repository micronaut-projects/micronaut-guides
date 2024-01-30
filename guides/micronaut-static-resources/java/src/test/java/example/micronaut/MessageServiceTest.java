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

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest(startApplication = false) // <1>
class MessageServiceTest {

    @Inject
    MessageService service; // <2>

    @Test
    void testItWorks() {
        assertEquals("Hello Tim!", service.sayHello("Tim"));
    }

    @Test
    void testValidationWithNull() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> service.sayHello(null));
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("sayHello.name: must not be blank", exception.getLocalizedMessage());
    }

    @Test
    void testValidationWithBlank() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> service.sayHello("   "));
        assertEquals(1, exception.getConstraintViolations().size());
        assertEquals("sayHello.name: must not be blank", exception.getMessage());
    }
}
