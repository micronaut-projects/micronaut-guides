package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
class StoreControllerTest(@Client("/store") val client: HttpClient) {

    @Test
    fun testInventoryItem() {
        val request = HttpRequest.GET<Any>("/inventory/laptop")
        val inventory = client.toBlocking().retrieve(request, HashMap::class.java)

        Assertions.assertNotNull(inventory)
        Assertions.assertEquals("laptop", inventory["item"])
        Assertions.assertEquals(4, inventory["store"])
        Assertions.assertNotNull(inventory["warehouse"])
    }

    @Test
    fun testInventoryItemNotFound() {
        val inventory = client.toBlocking().retrieve("/inventory/chair", HashMap::class.java)

        Assertions.assertNotNull(inventory)
        Assertions.assertEquals("chair", inventory["item"])
        Assertions.assertEquals("Not available at store", inventory["note"])
    }

    @Test
    fun testInventoryAll() {
        val request = HttpRequest.GET<Any>("/inventory")
        val inventory = client.toBlocking().retrieve(request, Argument.listOf(HashMap::class.java))

        Assertions.assertNotNull(inventory)
        Assertions.assertEquals(3, inventory.size)

        val names = inventory.map{ it["item"] } as List<String>
        Assertions.assertTrue(names.containsAll(listOf("desktop", "monitor", "laptop")))
    }

    @Test
    fun testOrder() {
        val item = java.util.Map.of("item", "desktop", "count", 8)
        val request = HttpRequest.POST<Any>("/order", item)
        val response = client.toBlocking().exchange<Any, Any>(request)

        Assertions.assertEquals(HttpStatus.CREATED, response.status)

        val inventory = client.toBlocking().retrieve("/inventory/desktop", HashMap::class.java)

        Assertions.assertNotNull(inventory)
        Assertions.assertEquals("desktop", inventory["item"])
        Assertions.assertEquals(10, inventory["store"])
        Assertions.assertNull(inventory["warehouse"])
    }
}