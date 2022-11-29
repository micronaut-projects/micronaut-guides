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
import io.micronaut.validation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.Valid;

@Controller("/api") // <1>
@Validated
public class GatewayController {

    private final OrdersClient orderClient;
    private final UsersClient userClient;

    public GatewayController(OrdersClient orderClient, UsersClient userClient) {
        this.orderClient = orderClient;
        this.userClient = userClient;
    }

    @Get("/users/{id}") // <2>
    public Mono<User> getUserById(@NonNull Integer id) {
        return userClient.getById(id).publishOn(Schedulers.boundedElastic());
    }

    @Get("/orders/{id}") // <3>
    public Mono<Order> getOrdersById(@NonNull Integer id) {
        return orderClient.getOrderById(id).publishOn(Schedulers.boundedElastic()).map(
                x -> new Order(x.id(), null, getUserById(x.userId()).publishOn(Schedulers.boundedElastic()).block(), x.items(), x.itemIds(), x.total())
        );
    }

    @Get("/items/{id}") // <4>
    public Mono<Item> getItemsById(@NonNull Integer id) {
        return orderClient.getItemsById(id).publishOn(Schedulers.boundedElastic());
    }

    @Get("/users") // <5>
    public Flux<User> getUsers() {
        return userClient.getUsers().publishOn(Schedulers.boundedElastic());
    }

    @Get("/items") // <6>
    public Flux<Item> getItems() {
        return orderClient.getItems().publishOn(Schedulers.boundedElastic());
    }

    @Get("/orders") // <7>
    public Flux<Order> getOrders() {
        return orderClient.getOrders().publishOn(Schedulers.boundedElastic()).map(
                x -> new Order(x.id(), null, getUserById(x.userId()).publishOn(Schedulers.boundedElastic()).block(), x.items(), x.itemIds(), x.total())
        );
    }

    @Post("/orders") // <8>
    public Mono<Order> createOrder(@Body @Valid Order order) {
        return getUserById(order.userId()).publishOn(Schedulers.boundedElastic())
                .map(
                        x -> {
                            Order createdOrder = orderClient.createOrder(order).publishOn(Schedulers.boundedElastic()).block();
                            createdOrder = new Order(createdOrder.id(), null, x, createdOrder.items(), createdOrder.itemIds(), createdOrder.total());
                            return createdOrder;
                        }
                ).switchIfEmpty(
                        Mono.error(new HttpStatusException(HttpStatus.BAD_REQUEST, String.format("User with id %s doesn't exist", order.userId())))
                );
    }

    @Post("/users")  // <9>
    public Mono<User> createUser(@Body @NonNull User user) {
        return userClient.createUser(user).publishOn(Schedulers.boundedElastic());
    }

}
