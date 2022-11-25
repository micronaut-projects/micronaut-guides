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
class GatewayController(ordersClient: OrdersClient, usersClient: UsersClient) {

    private val ordersClient: OrdersClient
    private val userClient: UsersClient

    init {
        this.ordersClient = ordersClient
        this.userClient = usersClient
    }


    @Get("/users/{id}") // <2>
    fun getUserById(@NonNull id: Int?): Mono<User?> {
        return userClient.getById(id).publishOn(Schedulers.boundedElastic())
    }

    @Get("/orders/{id}") // <3>
    fun getOrdersById(@NonNull id: Int?): Mono<Order>? {
        return ordersClient.getOrderById(id).publishOn(Schedulers.boundedElastic()).map { x: Order ->
            Order(
                x.id,
                null,
                getUserById(x.userId).publishOn(Schedulers.boundedElastic()).block(),
                x.items,
                x.itemIds,
                x.total
            )
        }
    }

    @Get("/items/{id}") // <4>
    fun getItemsById(@NonNull id: Int?): Mono<Item?>? {
        return ordersClient.getItemsById(id).publishOn(Schedulers.boundedElastic())
    }

    @Get("/users") // <5>
    fun getUsers(): Flux<User?>? {
        return userClient.users.publishOn(Schedulers.boundedElastic())
    }

    @Get("/items") // <6>
    fun getItems(): Flux<Item?>? {
        return ordersClient.items.publishOn(Schedulers.boundedElastic())
    }

    @Get("/orders") // <7>
    fun getOrders(): Flux<Order>? {
        return ordersClient.orders.publishOn(Schedulers.boundedElastic()).map { x: Order ->
            Order(
                x.id,
                null,
                getUserById(x.userId).publishOn(Schedulers.boundedElastic()).block(),
                x.items,
                x.itemIds,
                x.total
            )
        }
    }

    @Post("/orders") // <8>
    fun createOrder(@Body order: @Valid Order?): Mono<Order>? {
        return getUserById(order!!.userId).publishOn(Schedulers.boundedElastic())
            .map { x: User? ->
                var createdOrder: Order? =
                    ordersClient.createOrder(order).publishOn(Schedulers.boundedElastic()).block()
                createdOrder = Order(
                    createdOrder!!.id,
                    null,
                    x,
                    createdOrder.items,
                    createdOrder.itemIds,
                    createdOrder.total
                )
                createdOrder
            }.switchIfEmpty(
                Mono.error(
                    HttpStatusException(
                        HttpStatus.BAD_REQUEST,
                        String.format("User with %s id doesn't exist", order.userId)
                    )
                )
            )
    }

    @Post("/users") // <9>
    fun createUser(@Body @NonNull user: User?): Mono<User?>? {
        return userClient.createUser(user).publishOn(Schedulers.boundedElastic())
    }
}