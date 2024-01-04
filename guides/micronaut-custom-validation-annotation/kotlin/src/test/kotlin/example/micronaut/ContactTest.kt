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
package example.micronaut

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false) // <1>
class ContactTest(private val validator: Validator) { // <2>

    @Test
    fun contactValidation() {
        assertTrue(validator.validate(Contact("+14155552671")).isEmpty())
        val violationSet = validator.validate(Contact("+1-4155552671"))
        assertFalse(violationSet.isEmpty())

        val template = "{example.micronaut.E164.message}"

        assertTrue(
            violationSet
                .any {
                    it.messageTemplate == template &&
                            it.invalidValue == "+1-4155552671" &&
                            it.message == "must be a phone in E.164 format"
                })
    }
}