package example.micronaut.controllers;

import example.micronaut.models.Item;
import example.micronaut.models.Order;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller("/orders")  // <1>
@Secured(SecurityRule.IS_AUTHENTICATED)  // <2>
public class OrdersController {

    private final List<Order> orders = new ArrayList<>();

    @Get("/{id}")  // <3>
    public Mono<Order> findById(@NotNull Integer id) {
        return Mono.justOrEmpty(orders.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst());
    }

    @Get  // <4>
    public Flux<Order> getOrders() {
        return Flux.fromStream(orders.stream());
    }

    @Post  // <5>
    public Mono<Order> createOrder(@Body @Valid Order order) {
        order.setId(orders.size() + 1);
        List<Item> items = order.getItemIds().stream().map(
                x -> Item.items.stream().filter(
                        y -> y.getId().equals(x)
                ).findFirst().orElseThrow(
                        () -> new HttpStatusException(HttpStatus.BAD_REQUEST, String.format("Item with id %s doesn't exists", x))
                )
        ).collect(Collectors.toList());

        if (items.isEmpty()) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Items must be supplied");
        }
        order.setItems(items);
        order.setTotal(order.getItems().stream().map(Item::getPrice).reduce(BigDecimal::add).orElse(new BigDecimal("0")));
        orders.add(order);
        return Mono.just(order);
    }

}
