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
package example.micronaut

import io.micronaut.http.HttpStatus.CREATED
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle

@MicronautTest
class FruitControllerTest {

    @Test
    fun fruitsEndpointInteractsWithMongo(fruitClient: FruitClient) {

        var fruits = fruitClient.findAll()
        assertTrue(fruits.isEmpty())

        var status = fruitClient.save(Fruit("banana"))
        assertEquals(CREATED, status)

        fruits = fruitClient.findAll()
        assertFalse(fruits.isEmpty())
        assertEquals("banana", fruits[0].name)
        assertNull(fruits[0].description)

        status = fruitClient.save(Fruit("Apple", "Keeps the doctor away"))
        assertEquals(CREATED, status)

        fruits = fruitClient.findAll()
        assertTrue(fruits.any { (_, description): Fruit -> "Keeps the doctor away" == description })
    }
}
