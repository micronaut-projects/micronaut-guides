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

import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest
class FruitControllerTest {

    @Inject
    lateinit var fruitClient: FruitClient

    @Inject
    lateinit var fruitRepository: FruitRepository

    @AfterEach
    fun cleanup() {
        fruitRepository.deleteAll()
    }

    @Test
    fun emptyDatabaseContainsNoFruit() {
        assertEquals(0, fruitClient.list().count())
    }

    @Test
    fun testInteractionWithTheController() {
        var response = fruitClient.save(Fruit("banana", null))
        assertEquals(HttpStatus.CREATED, response.status)
        val banana = response.body()!!

        var fruits = fruitClient.list()
        var fruitList = fruits.toList()
        assertEquals(1, fruitList.size)
        assertEquals(banana.name, fruitList[0].name)
        assertNull(fruitList[0].description)

        response = fruitClient.save(Fruit("apple", "Keeps the doctor away"))
        assertEquals(HttpStatus.CREATED, response.status)

        fruits = fruitClient.list()
        assertTrue(fruits.any { it.description == "Keeps the doctor away" })

        banana.description = "Yellow and curved"
        fruitClient.update(banana)

        fruits = fruitClient.list()

        assertEquals(
            setOf("Keeps the doctor away", "Yellow and curved"),
            fruits.map { it.description }.toSet()
        )
    }

    @Test
    fun testSearchWorksAsExpected() {
        fruitClient.save(Fruit("apple", "Keeps the doctor away"))
        fruitClient.save(Fruit("pineapple", "Delicious"))
        fruitClient.save(Fruit("lemon", "Lemonentary my dear Dr Watson"))

        val fruit = fruitClient.query(listOf("apple", "pineapple"))

        assertTrue(fruit.all { it.name == "apple" || it.name == "pineapple" })
    }
}
