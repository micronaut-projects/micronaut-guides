package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.http.HttpStatus
import io.micronaut.runtime.server.EmbeddedServer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.stream.Collectors
import java.util.stream.Stream
import java.util.stream.StreamSupport
import kotlin.streams.toList

class FruitControllerTest : BaseTest() {

    @Test
    fun testInteractionWithTheController() {
        val apple = FruitCommand("apple", "Keeps the doctor away")
        val bananaName = "banana"
        val bananaDescription = "Yellow and curved"
        val properties: Map<String, Any> = super.getProperties()
        ApplicationContext.run(EmbeddedServer::class.java, properties).use { embeddedServer ->  // <1>
            val fruitClient = embeddedServer.applicationContext.getBean(FruitClient::class.java)
            var response = fruitClient.create(FruitCommand(bananaName))
            assertEquals(HttpStatus.CREATED, response.status)
            assertTrue(response.body.isPresent)
            val banana = response.body.get()

            val fruitList = fruitsList(fruitClient)
            assertEquals(1, fruitList.size)
            assertEquals(banana.name, fruitList[0].name)
            assertNull(fruitList[0].description)

            var bananaOptional: Fruit? = fruitClient.update(apple)
            assertNull(bananaOptional)

            response = fruitClient.create(apple)
            assertEquals(HttpStatus.CREATED, response.status)

            assertTrue(
                fruitsStream(fruitClient)
                    .anyMatch { (_, description): Fruit -> "Keeps the doctor away" == description }
            )
            bananaOptional = fruitClient.update(FruitCommand(bananaName, bananaDescription))
            Assertions.assertNotNull(bananaOptional)
            assertEquals(
                Stream.of("Keeps the doctor away", "Yellow and curved")
                    .collect(Collectors.toSet()),
                fruitsStream(fruitClient).map { it.description }.toList().toSet()
            )
        }
        ApplicationContext.run(EmbeddedServer::class.java, properties).use { embeddedServer ->  // <1>
            val fruitClient =
                embeddedServer.applicationContext.getBean(FruitClient::class.java)
            assertEquals(2, numberOfFruits(fruitClient))
            fruitClient.delete(apple)
            fruitClient.delete(FruitCommand(bananaName, bananaDescription))
        }
        ApplicationContext.run(EmbeddedServer::class.java, properties).use { embeddedServer ->  // <1>
            val fruitClient =
                embeddedServer.applicationContext.getBean(FruitClient::class.java)
            assertEquals(0, numberOfFruits(fruitClient))
        }
    }

    private fun numberOfFruits(fruitClient: FruitClient): Int {
        return fruitsList(fruitClient).size
    }

    private fun fruitsList(fruitClient: FruitClient): List<Fruit> {
        return fruitsStream(fruitClient)
            .collect(Collectors.toList())
    }

    private fun fruitsStream(fruitClient: FruitClient): Stream<Fruit> {
        val fruits: Iterable<Fruit> = fruitClient.list()
        return StreamSupport.stream(fruits.spliterator(), false)
    }

}