package example.micronaut.controllers

import example.micronaut.clients.OrdersClient
import example.micronaut.clients.UsersClient
import example.micronaut.models.Item
import example.micronaut.models.Order
import example.micronaut.models.User
import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.validation.Validated
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

import javax.validation.Valid

@Controller("/api") // <1>
@Validated
class GatewayController {

    private final OrdersClient ordersClient
    private final UsersClient userClient

    GatewayController(OrdersClient ordersClient, UsersClient userClient) {
        this.ordersClient = ordersClient
        this.userClient = userClient
    }

    @Get("/users/{id}") // <2>
    Mono<User> getUserById(@NonNull Integer id) {
         userClient.getById(id).publishOn(Schedulers.boundedElastic())
    }

    @Get("/orders/{id}") // <3>
    Mono<Order> getOrdersById(@NonNull Integer id) {
        ordersClient.getOrderById(id).publishOn(Schedulers.boundedElastic()).map(
                x -> new Order(
                        x.id, null, getUserById(x.userId).publishOn(Schedulers.boundedElastic()).block(), x.items, x.itemIds, x.total)
        )
    }

    @Get("/items/{id}") // <4>
    Mono<Item> getItemsById(@NonNull Integer id) {
        ordersClient.getItemsById(id).publishOn(Schedulers.boundedElastic())
    }

    @Get("/users") // <5>
    Flux<User> getUsers() {
        userClient.getUsers().publishOn(Schedulers.boundedElastic())
    }

    @Get("/items") // <6>
    Flux<Item> getItems() {
        ordersClient.getItems().publishOn(Schedulers.boundedElastic())
    }

    @Get("/orders") // <7>
    Flux<Order> getOrders() {
        ordersClient.getOrders().publishOn(Schedulers.boundedElastic()).map(
                x -> new Order(x.id, null, getUserById(x.userId).publishOn(Schedulers.boundedElastic()).block(), x.items, x.itemIds, x.total)
        )
    }

    @Post("/orders") // <8>
    Mono<Order> createOrder(@Body @Valid Order order) {
        User user = getUserById(order.userId).publishOn(Schedulers.boundedElastic()).block()
        if (user == null) {
           return Mono.error(new HttpStatusException(HttpStatus.BAD_REQUEST, String.format("User with id %s doesn't exist", order.userId)))
        }
        def createdOrder = ordersClient.createOrder(order).publishOn(Schedulers.boundedElastic()).block()
        Mono.just(new Order(createdOrder.id, null, user, createdOrder.items, createdOrder.itemIds , createdOrder.total))
    }

    @Post("/users")  // <9>
    Mono<User> createUser(@Body @NonNull User user) {
        userClient.createUser(user).publishOn(Schedulers.boundedElastic())
    }

}
