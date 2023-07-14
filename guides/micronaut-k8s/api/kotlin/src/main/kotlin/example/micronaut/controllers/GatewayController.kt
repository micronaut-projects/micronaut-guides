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
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import jakarta.validation.Valid

@Controller("/api") // <1>
@ExecuteOn(TaskExecutors.IO) // <2>
class GatewayController(ordersClient: OrdersClient, usersClient: UsersClient) {

    private val ordersClient: OrdersClient
    private val userClient: UsersClient

    init {
        this.ordersClient = ordersClient
        this.userClient = usersClient
    }


    @Get("/users/{id}") // <3>
    fun getUserById(@NonNull id: Int): User? {
        return userClient.getById(id)
    }

    @Get("/orders/{id}") // <4>
    fun getOrdersById(@NonNull id: Int): Order {
        val order = ordersClient.getOrderById(id)

        return Order(
            order.id,
            null,
            getUserById(order.userId!!),
            order.items,
            order.itemIds,
            order.total
        )
    }

    @Get("/items/{id}") // <5>
    fun getItemsById(@NonNull id: Int): Item {
        return ordersClient.getItemsById(id)
    }

    @Get("/users") // <6>
    fun getUsers(): List<User> {
        return userClient.users
    }

    @Get("/items") // <7>
    fun getItems(): List<Item> {
        return ordersClient.items
    }

    @Get("/orders") // <8>
    fun getOrders(): List<Order>? {
        val orders = mutableListOf<Order>()
        ordersClient.orders.forEach{orders.add(Order(
            it.id,
            null,
            getUserById(it.userId!!),
            it.items,
            it.itemIds,
            it.total))
        }
        return orders
    }

    @Post("/orders") // <9>
    fun createOrder(@Body order: @Valid Order): Order? {
        val user = getUserById(order!!.userId!!)
            ?: throw  HttpStatusException(
                HttpStatus.BAD_REQUEST,
                String.format("User with id %s doesn't exist", order.userId)
            )

        val createdOrder = ordersClient.createOrder(order)

        return Order(
            createdOrder!!.id,
            null,
            user,
            createdOrder.items,
            createdOrder.itemIds,
            createdOrder.total
        )
    }

    @Post("/users") // <10>
    fun createUser(@Body @NonNull user: User): User {
        return userClient.createUser(user)
    }
}
