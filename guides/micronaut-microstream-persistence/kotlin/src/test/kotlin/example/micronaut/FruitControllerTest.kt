package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FruitControllerTest : BaseMicroStreamTest() {

    @Test
    fun emptyDatabaseContainsNoFruit() {
        Assertions.assertEquals(0, fruitClient.list().toList().size)
    }

    @Test
    fun testInteractionWithTheController() {
        var response: HttpResponse<Fruit> = fruitClient.create(FruitCommand("banana", null))

        Assertions.assertEquals(HttpStatus.CREATED, response.status)

        val (name) = response.body.get()
        var fruitList = fruitClient.list().toList()

        Assertions.assertEquals(1, fruitList.size)
        Assertions.assertEquals(name, fruitList[0].name)
        Assertions.assertNull(fruitList[0].description)

        response = fruitClient.create(FruitCommand("apple", "Keeps the doctor away"))

        Assertions.assertEquals(HttpStatus.CREATED, response.status)

        fruitList = fruitClient.list().toList()
        Assertions.assertTrue(fruitList.any { "Keeps the doctor away" == it.description })

        fruitClient.update(FruitCommand("banana", "Yellow and curved"))
        fruitList = fruitClient.list().toList()

        Assertions.assertEquals(
            setOf("Keeps the doctor away", "Yellow and curved"),
            fruitList.map { it.description }.toSet()
        )
    }
}