package example.micronaut.clients;

import example.micronaut.models.Item;
import example.micronaut.models.Order;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

import java.util.List;

@Client("orders") // <1>
public interface OrdersClient {
    @Get("/orders/{id}")
    Order getOrderById(int id);

    @Post("/orders")
    Order createOrder(@Body Order order);

    @Get("/orders")
    List<Order> getOrders();

    @Get("/items")
    List<Item> getItems();

    @Get("/items/{id}")
    Item getItemsById(int id);
}

