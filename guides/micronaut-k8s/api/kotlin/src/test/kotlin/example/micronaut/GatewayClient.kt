package example.micronaut

import example.micronaut.models.Item
import example.micronaut.models.Order
import example.micronaut.models.User
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("/") // <1>
interface GatewayClient {
    @Get("/api/items/{id}")
    fun getItemById(id: Int?): Item?

    @Get("/api/orders/{id}")
    fun getOrderById(id: Int?): Order?

    @Get("/api/users/{id}")
    fun getUsersById(id: Int?): User?

    @get:Get("/api/users")
    val users: List<User>

    @get:Get("/api/items")
    val items: List<Item>

    @get:Get("/api/orders")
    val orders: List<Order>

    @Post("/api/orders")
    fun createOrder(@Body order: Order): Order

    @Post("/api/users")
    fun createUser(@Body user: User): User
}
