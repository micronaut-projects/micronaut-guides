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
        return userClient.getById(id);
    }

    @Get("/orders/{id}") // <3>
    public Mono<Order> getOrdersById(@NonNull Integer id) {
        return orderClient.getOrderById(id).publishOn(Schedulers.boundedElastic()).map(
                x -> {
                    x.setUser(getUserById(x.getUserId()).block());
                    return x;
                }
        );
    }

    @Get("/items/{id}") // <4>
    public Mono<Item> getItemsById(@NonNull Integer id) {
        return orderClient.getItemsById(id);
    }

    @Get("/users") // <5>
    public Flux<User> getUsers() {
        return userClient.getUsers();
    }

    @Get("/items") // <6>
    public Flux<Item> getItems() {
        return orderClient.getItems();
    }

    @Get("/orders") // <7>
    public Flux<Order> getOrders() {
        return orderClient.getOrders().publishOn(Schedulers.boundedElastic()).map(
                x -> {
                    x.setUser(getUserById(x.getUserId()).block());
                    return x;
                }
        );
    }

    @Post("/orders") // <8>
    public Mono<Order> createOrder(@Body @Valid Order order) {
        return getUserById(order.getUserId())
                .map(
                        x -> {
                            Order createdOrder = orderClient.createOrder(order).block();
                            createdOrder.setUser(x);
                            return createdOrder;
                        }
                ).switchIfEmpty(
                        Mono.error(new HttpStatusException(HttpStatus.BAD_REQUEST, String.format("User with %s id doesn't exist", order.getUserId())))
                );
    }

    @Post("/users")  // <9>
    public Mono<User> createUser(@Body @NonNull User user) {
        return userClient.createUser(user);
    }

}
