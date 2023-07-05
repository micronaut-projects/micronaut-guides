package example.micronaut.controllers;

import example.micronaut.clients.OrdersClient;
import example.micronaut.clients.UsersClient;
import example.micronaut.models.Item;
import example.micronaut.models.Order;
import example.micronaut.models.User;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.validation.Valid;
import java.util.List;

@Controller("/api") // <1>
@ExecuteOn(TaskExecutors.IO) // <2>
class GatewayController {

    private final OrdersClient orderClient;
    private final UsersClient userClient;

    GatewayController(OrdersClient orderClient, UsersClient userClient) {
        this.orderClient = orderClient;
        this.userClient = userClient;
    }

    @Get("/users/{id}") // <3>
    User getUserById(int id) {
        return userClient.getById(id);
    }

    @Get("/orders/{id}") // <4>
    Order getOrdersById(int id) {
        Order order = orderClient.getOrderById(id);
        return new Order(order.id(), null, getUserById(order.userId()), order.items(), order.itemIds(), order.total());
    }

    @Get("/items/{id}") // <5>
    Item getItemsById(int id) {
        return orderClient.getItemsById(id);
    }

    @Get("/users") // <6>
    List<User> getUsers() {
        return userClient.getUsers();
    }

    @Get("/items") // <7>
    List<Item> getItems() {
        return orderClient.getItems();
    }

    @Get("/orders") // <8>
    List<Order> getOrders() {
        return orderClient.getOrders()
                .stream()
                .map(x -> new Order(x.id(), null, getUserById(x.userId()), x.items(), x.itemIds(), x.total()))
                .toList();
    }

    @Post("/orders") // <9>
    Order createOrder(@Body @Valid Order order) {
        User user = getUserById(order.userId());
        if (user == null) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, String.format("User with id %s doesn't exist", order.userId()));
        }
        Order createdOrder = orderClient.createOrder(order);
        return new Order(createdOrder.id(), null, user, createdOrder.items(), createdOrder.itemIds(), createdOrder.total());
    }

    @Post("/users")  // <10>
    User createUser(@Body @NonNull User user) {
        return userClient.createUser(user);
    }

}
