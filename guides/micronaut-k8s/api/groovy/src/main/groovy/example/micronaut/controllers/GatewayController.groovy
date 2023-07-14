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

import jakarta.validation.Valid

@Controller("/api") // <1>
@ExecuteOn(TaskExecutors.IO) // <2>
class GatewayController {

    private final OrdersClient ordersClient
    private final UsersClient userClient

    GatewayController(OrdersClient ordersClient, UsersClient userClient) {
        this.ordersClient = ordersClient
        this.userClient = userClient
    }

    @Get("/users/{id}") // <3>
    User getUserById(int id) {
        userClient.getById(id)
    }

    @Get("/orders/{id}") // <4>
    Order getOrdersById(int id) {
        def order = ordersClient.getOrderById(id)
        new Order(order.id, null, getUserById(order.userId), order.items, order.itemIds, order.total)
    }

    @Get("/items/{id}") // <5>
    Item getItemsById(int id) {
        ordersClient.getItemsById(id)
    }

    @Get("/users") // <6>
    List<User> getUsers() {
        userClient.getUsers()
    }

    @Get("/items") // <7>
    List<Item> getItems() {
        ordersClient.getItems()
    }

    @Get("/orders") // <8>
    List<Order> getOrders() {
        def orders = []
        ordersClient.getOrders().each {
            x -> orders.add(new Order(x.id, null, getUserById(x.userId), x.items, x.itemIds, x.total))
        }
        orders
    }

    @Post("/orders") // <9>
    Order createOrder(@Body @Valid Order order) {
        User user = getUserById(order.userId)
        if (user == null) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, String.format("User with id %s doesn't exist", order.userId))
        }
        def createdOrder = ordersClient.createOrder(order)
        new Order(createdOrder.id, null, user, createdOrder.items, createdOrder.itemIds , createdOrder.total)
    }

    @Post("/users")  // <10>
    User createUser(@Body @NonNull User user) {
        userClient.createUser(user)
    }

}
