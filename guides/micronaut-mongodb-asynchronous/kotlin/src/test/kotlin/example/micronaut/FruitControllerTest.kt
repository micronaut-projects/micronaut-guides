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
@TestInstance(Lifecycle.PER_CLASS)
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
