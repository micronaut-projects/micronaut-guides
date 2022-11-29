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
    Mono<Order> getOrderById(Integer id)

    @Post("/orders")
    Mono<Order> createOrder(@Body Order order)

    @Get("/orders")
    Flux<Order> getOrders()

    @Get("/items")
    Flux<Item> getItems()

    @Get("/items/{id}")
    Mono<Item> getItemsById(Integer id)
}