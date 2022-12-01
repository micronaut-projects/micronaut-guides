package example.micronaut

import example.micronaut.clients.OrdersClient
import example.micronaut.clients.UsersClient
import example.micronaut.models.Item
import example.micronaut.models.Order
import example.micronaut.models.User
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.math.BigDecimal

@MicronautTest // <1>
class GatewayControllerTest {
    @Inject
    var ordersClient: OrdersClient? = null

    @Inject
    var usersClient: UsersClient? = null

    @Inject
    var gatewayClient: GatewayClient? = null

    @MockBean(OrdersClient::class)
    fun ordersClient(): OrdersClient {
        return mock(OrdersClient::class.java)
    }

    @MockBean(UsersClient::class)
    fun usersClient(): UsersClient {
        return mock(UsersClient::class.java)
    }

    @Test
    fun itemById() {
        val itemId = 1
        val item = Item(itemId, "test", BigDecimal.ONE)
        `when`(ordersClient!!.getItemsById(1)).thenReturn(item)
        val retrievedItem = gatewayClient!!.getItemById(item.id)
        assertEquals(item.id, retrievedItem!!.id)
        assertEquals(item.name, retrievedItem.name)
        assertEquals(item.price, retrievedItem.price)
    }

    @Test
    fun orderById() {
        val order = Order(1, 2, null, null, ArrayList(), null)
        val user = User(order.userId!!, "firstName", "lastName", "test")
        `when`(ordersClient!!.getOrderById(1)).thenReturn(order)
        `when`(usersClient!!.getById(user.id)).thenReturn(user)
        val retrievedOrder = gatewayClient!!.getOrderById(order.id)
        assertEquals(order.id, retrievedOrder!!.id)
        assertEquals(order.userId, retrievedOrder.user!!.id)
        assertNull(retrievedOrder.userId)
        assertEquals(user.username, retrievedOrder.user!!.username)
    }

    @Test
    fun userById() {
        val user = User(1, "firstName", "lastName", "test")
        `when`(usersClient!!.getById(1)).thenReturn(user)
        val retrievedUser = gatewayClient!!.getUsersById(user.id)
        assertEquals(user.id, retrievedUser!!.id)
        assertEquals(user.username, retrievedUser.username)
    }

    @Test
    fun users() {
        val user = User(1, "firstName", "lastName", "test")
        `when`(usersClient!!.users).thenReturn(listOf(user))
        val users = gatewayClient!!.users
        assertNotNull(users)
        assertEquals(1, users.size)
        assertEquals(user.id, users[0].id)
        assertEquals(user.username, users[0].username)
    }

    @Test
    fun items() {
        val item = Item(1, "test", BigDecimal.ONE)
        `when`(ordersClient!!.items).thenReturn(listOf(item))
        val items = gatewayClient!!.items
        assertNotNull(items)
        assertEquals(1, items.size)
        assertEquals(item.name, items[0].name)
        assertEquals(item.price, items[0].price)
    }

    @Test
    fun orders() {
        val order = Order(1, 2, null, null, ArrayList(), null)
        val user = User(order.userId!!, "firstName", "lastName", "test")
        `when`(ordersClient!!.orders).thenReturn(listOf(order))
        `when`(usersClient!!.getById(order.userId!!)).thenReturn(user)
        val orders = gatewayClient!!.orders
        assertNotNull(orders)
        assertEquals(1, orders.size)
        assertNull(orders[0].userId)
        assertEquals(user.id, orders[0].user!!.id)
        assertEquals(order.id, orders[0].id)
        assertEquals(user.username, orders[0].user!!.username)
    }

    @Test
    fun createUser() {
        val firstName = "firstName"
        val lastName = "lastName"
        val username = "username"
        val user = User(0, firstName, lastName, username)
        `when`(usersClient!!.createUser(any())).thenReturn(user)
        val createdUser = gatewayClient!!.createUser(user)
        assertEquals(firstName, createdUser.firstName)
        assertEquals(lastName, createdUser.lastName)
        assertEquals(username, createdUser.username)
    }

    @Test
    fun createOrder() {
        val order = Order(1, 2, null, null, ArrayList(), null)
        val user = User(order.userId!!, "firstName", "lastName", "test")
        `when`(usersClient!!.getById(user.id)).thenReturn(user)
        `when`(ordersClient!!.createOrder(any())).thenReturn(order)
        val createdOrder = gatewayClient!!.createOrder(order)
        assertEquals(order.id, createdOrder.id)
        assertNull(createdOrder.userId)
        assertEquals(order.userId, createdOrder.user!!.id)
        assertEquals(user.username, createdOrder.user!!.username)
    }

    @Test
    fun createOrderUserDoesntExists() {
        val order = Order(1, 2, null, null, ArrayList(), BigDecimal(0))
        `when`(ordersClient!!.createOrder(any())).thenReturn(order)
        `when`(usersClient!!.getById(order.userId!!)).thenReturn(null)
        val exception = assertThrows(
            HttpClientResponseException::class.java
        ) { gatewayClient!!.createOrder(order) }
        assertEquals(exception.status, HttpStatus.BAD_REQUEST)
        assertTrue(
            exception.response.getBody(String::class.java).orElse("").contains("User with id 2 doesn't exist")
        )
    }

    @Test
    fun exceptionHandler() {
        val user = User(1, "firstname", "lastname", "username")
        val message = "Test error message"
        `when`(usersClient!!.createUser(any())).thenThrow(
            HttpClientResponseException(
                "Test",
                HttpResponse.badRequest(message)
            )
        )
        val exception = assertThrows(
            HttpClientResponseException::class.java
        ) { gatewayClient!!.createUser(user) }
        assertEquals(exception.status, HttpStatus.BAD_REQUEST)
        assertTrue(exception.response.getBody(String::class.java).orElse("").contains("Test error message"))
    }
}
