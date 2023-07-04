package example.micronaut;

import example.micronaut.models.Item;
import example.micronaut.models.Order;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;

import java.util.List;

@Client("/") // <1>
interface OrderItemClient {

    @Get("/orders/{id}")
    Order getOrderById(@Header String authorization, int id);

    @Post("/orders")
    Order createOrder(@Header String authorization, @Body Order order);

    @Get("/orders")
    List<Order> getOrders(@Header String authorization);

    @Get("/items")
    List<Item> getItems(@Header String authorization);

    @Get("/items/{id}")
    Item getItemsById(@Header String authorization, int id);
}
