//tag::packageandimports[]
package example.micronaut.clients;

import example.micronaut.models.Item;
import example.micronaut.models.Order;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.retry.annotation.Recoverable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
//end::packageandimports[]

/*
//tag::harcoded[]
@Client("http://localhost:8081") // <1>
//end::harcoded[]
*/
//tag::k8s[]
@Client("orders") // <1>
//end::k8s[]
//tag::clazz[]
public interface OrdersClient {
    @Get("/orders/{id}")
    Mono<Order> getOrderById(Integer id);

    @Post("/orders")
    Mono<Order> createOrder(@Body Order order);

    @Get("/orders")
    Flux<Order> getOrders();

    @Get("/items")
    Flux<Item> getItems();

    @Get("/items/{id}")
    Mono<Item> getItemsById(Integer id);
}
//end::clazz[]
