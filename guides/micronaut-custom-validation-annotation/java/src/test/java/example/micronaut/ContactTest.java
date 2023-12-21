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
package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false) // <1>
class ContactTest {

    @Inject // <2>
    Validator validator;

    @Test
    void contactValidation() {
        assertTrue(validator.validate(new Contact("+14155552671")).isEmpty());
        Set<ConstraintViolation<Contact>> violationSet = validator.validate(new Contact("+1-4155552671"));
        assertFalse(violationSet.isEmpty());
        String template = "{example.micronaut.E164.message}";
        assertTrue(violationSet.stream().anyMatch(violation ->
                violation.getMessageTemplate().equals(template)
                        && violation.getInvalidValue().equals("+1-4155552671")
                        && violation.getMessage().equals("must be a phone in E.164 format"))
        );
    }
}
