package example.micronaut.clients

import example.micronaut.models.Item
import example.micronaut.models.Order
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Client("orders") // <1>
interface OrdersClient {
    @Get("/orders/{id}")
    fun getOrderById(id: Int): Order

    @Post("/orders")
    fun createOrder(@Body order: Order?): Order

    @get:Get("/orders")
    val orders: List<Order>

    @get:Get("/items")
    val items: List<Item>

    @Get("/items/{id}")
    fun getItemsById(id: Int): Item
}