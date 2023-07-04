package example.micronaut

import example.micronaut.models.Item
import example.micronaut.models.Order
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("/") // <1>
interface OrderItemClient {
    @Get("/orders/{id}")
    fun getOrderById(@Header authorization: String?, id: Int?): Order?

    @Post("/orders")
    fun createOrder(@Header authorization: String?, @Body order: Order?): Order

    @Get("/orders")
    fun getOrders(@Header authorization: String?): List<Order>

    @Get("/items")
    fun getItems(@Header authorization: String?): List<Item>

    @Get("/items/{id}")
    fun getItemsById(@Header authorization: String?, id: Int?): Item?
}