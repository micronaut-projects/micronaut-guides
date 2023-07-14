package example.micronaut.controllers;

import example.micronaut.models.Item;
import example.micronaut.models.Order;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

@Controller("/orders")  // <1>
@Secured(SecurityRule.IS_AUTHENTICATED)  // <2>
class OrdersController {

    private final List<Order> orders = new ArrayList<>();

    @Get("/{id}")  // <3>
    public Optional<Order> findById(int id) {
        return orders.stream()
                .filter(it -> it.id().equals(id))
                .findFirst();
    }

    @Get  // <4>
    public List<Order> getOrders() {
        return orders;
    }

    @Post  // <5>
    public Order createOrder(@Body @Valid Order order) {
        if (CollectionUtils.isEmpty(order.itemIds())) {
            throw new HttpStatusException(HttpStatus.BAD_REQUEST, "Items must be supplied");
        }

        List<Item> items = order.itemIds().stream().map(
                x -> Item.items.stream().filter(
                        y -> y.id().equals(x)
                ).findFirst().orElseThrow(
                        () -> new HttpStatusException(HttpStatus.BAD_REQUEST, String.format("Item with id %s doesn't exist", x))
                )

        ).toList();


        BigDecimal total = items.stream().map(Item::price).reduce(BigDecimal::add).orElse(new BigDecimal("0"));
        Order newOrder = new Order(orders.size() + 1, order.userId(), items, null, total);

        orders.add(newOrder);
        return newOrder;
    }

}
