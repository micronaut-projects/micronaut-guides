package example.micronaut

import example.micronaut.auth.Credentials
import example.micronaut.models.Order
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientException
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.Base64

@MicronautTest // <1>
class OrdersControllerTest {

    @Inject
    var orderItemClient: OrderItemClient? = null

    @Inject
    var credentials: Credentials? = null

    @Test
    fun testUnauthorized() {
        val exception = Assertions.assertThrows(
            HttpClientException::class.java
        ) { orderItemClient!!.getOrders("") }
        assertTrue(exception.message!!.contains("Unauthorized"))
    }

    @Test
    fun multipleOrderInteraction() {
        val authHeader = "Basic " + Base64.getEncoder()
            .encodeToString((credentials!!.username + ":" + credentials!!.password).toByteArray())
        val userId = 1
        val itemIds = listOf(1, 1, 2, 3)
        val order = Order(0, userId, null, itemIds, null)
        val createdOrder = orderItemClient!!.createOrder(authHeader, order)
        assertNotNull(createdOrder.items)
        assertEquals(4, createdOrder.items!!.size)
        assertEquals(BigDecimal("6.75"), createdOrder.total)
        assertEquals(userId, createdOrder.userId)
        val retrievedOrder = orderItemClient!!.getOrderById(authHeader, createdOrder.id)
        assertNotNull(retrievedOrder!!.items)
        assertEquals(4, retrievedOrder.items!!.size)
        assertEquals(BigDecimal("6.75"), retrievedOrder.total)
        assertEquals(userId, retrievedOrder.userId)
        val orders = orderItemClient!!.getOrders(authHeader)
        assertNotNull(orders)
        assertTrue(orders.stream()
            .map<Any>(Order::userId)
            .anyMatch { id: Any -> id == userId })
    }

    @Test
    fun itemDoesntExists() {
        val authHeader = "Basic " + Base64.getEncoder()
            .encodeToString((credentials!!.username + ":" + credentials!!.password).toByteArray())
        val userId = 1
        val itemIds = listOf(5)
        val order = Order(0, userId, null, itemIds, null)
        val exception = Assertions.assertThrows(
            HttpClientResponseException::class.java
        ) { orderItemClient!!.createOrder(authHeader, order) }
        assertEquals(exception.status, HttpStatus.BAD_REQUEST)
        assertTrue(
            exception.response.getBody(String::class.java).orElse("").contains("Item with id 5 doesn't exist")
        )
    }

    @Test
    fun orderEmptyItems() {
        val authHeader = "Basic " + Base64.getEncoder()
            .encodeToString((credentials!!.username + ":" + credentials!!.password).toByteArray())
        val userId = 1
        val order = Order(0, userId, null, null, null)
        val exception = Assertions.assertThrows(
            HttpClientResponseException::class.java
        ) { orderItemClient!!.createOrder(authHeader, order) }
        assertEquals(exception.status, HttpStatus.BAD_REQUEST)
        assertTrue(
            exception.response.getBody(String::class.java).orElse("").contains("Items must be supplied")
        )
    }
}