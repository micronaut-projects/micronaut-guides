/*
 * Copyright 2017-2026 original authors
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
import jakarta.inject.Inject
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false) // <1>
class MessageServiceTest {

    @Inject
    lateinit var service: MessageService // <2>

    @Test
    fun testItWorks() {
        assertEquals("Hello Tim!", service.sayHello("Tim"))
    }

    @Test
    fun testValidationWithNull() {
        val exception = assertThrows(ConstraintViolationException::class.java) { service.sayHello(null) }
        assertEquals(1, exception.constraintViolations.size)
        assertEquals("sayHello.name: must not be blank", exception.localizedMessage)
    }

    @Test
    fun testValidationWithBlank() {
        val exception = assertThrows(ConstraintViolationException::class.java) { service.sayHello("   ") }
        assertEquals(1, exception.constraintViolations.size)
        assertEquals("sayHello.name: must not be blank", exception.message)
    }
}
