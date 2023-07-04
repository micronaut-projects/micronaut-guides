package example.micronaut;

import example.micronaut.models.Item;
import example.micronaut.models.Order;
import example.micronaut.models.User;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

import java.util.List;

@Client("/") // <1>
public interface GatewayClient {

    @Get("/api/items/{id}")
    Item getItemById(int id);

    @Get("/api/orders/{id}")
    Order getOrderById(int id);

    @Get("/api/users/{id}")
    User getUsersById(int id);

    @Get("/api/users")
    List<User> getUsers();

    @Get("/api/items")
    List<Item> getItems();

    @Get("/api/orders")
    List<Order> getOrders();

    @Post("/api/orders")
    Order createOrder(@Body Order order);

    @Post("/api/users")
    User createUser(@Body User user);
}
