package example.micronaut

import example.micronaut.auth.Credentials
import example.micronaut.models.Item
import io.micronaut.http.client.exceptions.HttpClientException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.Base64

@MicronautTest // <1>
class ItemsControllerTest {
    @Inject
    var orderItemClient: OrderItemClient? = null

    @Inject
    var credentials: Credentials? = null

    @Test
    fun testUnauthorized() {
        val exception = assertThrows(
            HttpClientException::class.java
        ) { orderItemClient?.getItems("") }
        assertTrue(exception.message!!.contains("Unauthorized"))
    }

    @Test
    fun getItem() {
        val itemId = 1
        val authHeader = "Basic " + Base64.getEncoder()
            .encodeToString((credentials!!.username + ":" + credentials!!.password).toByteArray())
        val item: Item? = orderItemClient!!.getItemsById(authHeader, itemId)
        assertEquals(itemId, item!!.id)
        assertEquals("Banana", item.name)
        assertEquals(BigDecimal("1.5"), item.price)
    }

    @Test
    fun getItems() {
        val authHeader = "Basic " + Base64.getEncoder()
            .encodeToString((credentials!!.username + ":" + credentials!!.password).toByteArray())
        val items: List<Item> = orderItemClient!!.getItems(authHeader)
        assertNotNull(items)
        val existingItemNames = listOf("Kiwi", "Banana", "Grape")
        assertEquals(3, items.size)
        assertTrue(items.stream()
            .map<Any>(Item::name)
            .allMatch { name: Any ->
                existingItemNames.stream().anyMatch { x: String -> x == name }
            })
    }
}
