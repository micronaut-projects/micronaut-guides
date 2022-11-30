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
import io.micronaut.validation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller("/api") // <1>
@Validated
@ExecuteOn(TaskExecutors.IO)
public class GatewayController {

    private final OrdersClient orderClient;
    private final UsersClient userClient;

    public GatewayController(OrdersClient orderClient, UsersClient userClient) {
        this.orderClient = orderClient;
        this.userClient = userClient;
    }

    @Get("/users/{id}") // <2>
    public User getUserById(@NonNull Integer id) {
        return userClient.getById(id);
    }

    @Get("/orders/{id}") // <3>
    public Order getOrdersById(@NonNull Integer id) {
        Order order = orderClient.getOrderById(id);
        return new Order(order.id(), null, getUserById(order.userId()), order.items(), order.itemIds(), order.total());
    }

    @Get("/items/{id}") // <4>
    public Item getItemsById(@NonNull Integer id) {
        return orderClient.getItemsById(id);
    }

    @Get("/users") // <5>
    public List<User> getUsers() {
        return userClient.getUsers();
    }

    @Get("/items") // <6>
    public List<Item> getItems() {
        return orderClient.getItems();
    }

    @Get("/orders") // <7>
    public List<Order> getOrders() {
        List<Order> orders = new ArrayList<>();
        orderClient.getOrders().forEach(x-> orders.add(new Order(x.id(), null, getUserById(x.userId()), x.items(), x.itemIds(), x.total())));
        return orders;
    }

    @Post("/orders") // <8>
    public Order createOrder(@Body @Valid Order order) {
        User user = getUserById(order.userId());
        if (user == null) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, String.format("User with id %s doesn't exist", order.userId()));
        }
        Order createdOrder = orderClient.createOrder(order);
        return new Order(createdOrder.id(), null, user, createdOrder.items(), createdOrder.itemIds(), createdOrder.total());
    }

    @Post("/users")  // <9>
    public User createUser(@Body @NonNull User user) {
        return userClient.createUser(user);
    }

}
