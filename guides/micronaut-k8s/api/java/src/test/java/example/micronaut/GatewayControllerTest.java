package example.micronaut;

import example.micronaut.clients.OrdersClient;
import example.micronaut.clients.UsersClient;
import example.micronaut.models.Item;
import example.micronaut.models.Order;
import example.micronaut.models.User;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@MicronautTest
public class GatewayControllerTest {

    @Inject
    OrdersClient ordersClient;

    @Inject
    UsersClient usersClient;

    @Inject
    @Client("/")
    HttpClient client;

    @MockBean(OrdersClient.class)
    OrdersClient ordersClient() {
        return mock(OrdersClient.class);
    }

    @MockBean(UsersClient.class)
    UsersClient usersClient() {
        return mock(UsersClient.class);
    }


    @Test
    void getItemById() {
        int itemId = 1;
        Item item = new Item(itemId, "test", BigDecimal.ONE);

        when(ordersClient.getItemsById(1)).thenReturn(Mono.just(item));

        Item retrievedItem = client.toBlocking().retrieve(
                HttpRequest.GET("/api/items/" + itemId)
                , Item.class
        );

        assertEquals(item.getId(), retrievedItem.getId());
        assertEquals(item.getName(), retrievedItem.getName());
        assertEquals(item.getPrice(), retrievedItem.getPrice());

    }

    @Test
    void getOrderById() {
        Order order = new Order();
        order.setUserId(2);
        order.setId(1);

        User user = new User();
        user.setUsername("test");
        user.setId(order.getUserId());

        when(ordersClient.getOrderById(1)).thenReturn(Mono.just(order));

        when(usersClient.getById(user.getId())).thenReturn(Mono.just(user));


        Order retrievedOrder = client.toBlocking().retrieve(
                HttpRequest.GET("/api/orders/" + order.getId())
                , Order.class
        );

        assertEquals(order.getId(), retrievedOrder.getId());
        assertEquals(order.getUserId(), retrievedOrder.getUser().getId());
        assertEquals(order.getUserId(), retrievedOrder.getUserId());
        assertEquals(user.getUsername(), retrievedOrder.getUser().getUsername());
    }

    @Test
    void getUserById() {
        User user = new User();
        user.setUsername("test");
        user.setId(1);

        when(usersClient.getById(1)).thenReturn(Mono.just(user));

        User retrievedUser = client.toBlocking().retrieve(
                HttpRequest.GET("/api/users/" + user.getId())
                , User.class
        );

        assertEquals(user.getId(), retrievedUser.getId());
        assertEquals(user.getUsername(), retrievedUser.getUsername());
    }

    @Test
    void getUsers() {
        User user = new User();
        user.setUsername("test");
        user.setId(1);

        when(usersClient.getUsers()).thenReturn(Flux.just(user));

        HttpResponse<List<User>> rsp = client.toBlocking().exchange(
                HttpRequest.GET("/api/users"),
                Argument.listOf(User.class));

        assertEquals(HttpStatus.OK, rsp.getStatus());
        assertNotNull(rsp.body());
        List<User> users = rsp.body();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user.getId(), users.get(0).getId());
        assertEquals(user.getUsername(), users.get(0).getUsername());
    }

    @Test
    void getItems() {

        Item item = new Item(1, "test", BigDecimal.ONE);

        when(ordersClient.getItems()).thenReturn(Flux.just(item));

        HttpResponse<List<Item>> rsp = client.toBlocking().exchange(
                HttpRequest.GET("/api/items"),
                Argument.listOf(Item.class));

        assertEquals(HttpStatus.OK, rsp.getStatus());
        assertNotNull(rsp.body());
        List<Item> items = rsp.body();
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(item.getName(), items.get(0).getName());
        assertEquals(item.getPrice(), items.get(0).getPrice());
    }

    @Test
    void getOrders() {

        Order order = new Order();
        order.setUserId(2);
        order.setId(1);
        User user = new User();
        user.setUsername("test");
        user.setId(order.getUserId());

        when(ordersClient.getOrders()).thenReturn(Flux.just(order));

        when(usersClient.getById(order.getUserId())).thenReturn(Mono.just(user));


        HttpResponse<List<Order>> rsp = client.toBlocking().exchange(
                HttpRequest.GET("/api/orders"),
                Argument.listOf(Order.class));

        assertEquals(HttpStatus.OK, rsp.getStatus());
        assertNotNull(rsp.body());
        List<Order> orders = rsp.body();
        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertEquals(order.getUserId(), orders.get(0).getUserId());
        assertEquals(order.getUserId(), orders.get(0).getUser().getId());
        assertEquals(order.getId(), orders.get(0).getId());
        assertEquals(user.getUsername(), orders.get(0).getUser().getUsername());
    }

    @Test
    void createUser() {
        User user = new User();
        String firstName = "firstName";
        String lastName = "lastName";
        String username = "username";

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);

        when(usersClient.createUser(any())).thenReturn(Mono.just(user));

        User createdUser = client.toBlocking().retrieve(
                HttpRequest.POST("/api/users", user), User.class
        );

        assertEquals(firstName, createdUser.getFirstName());
        assertEquals(lastName, createdUser.getLastName());
        assertEquals(username, createdUser.getUsername());
    }

    @Test
    void createOrder() {
        Order order = new Order();
        order.setUserId(2);
        order.setId(1);
        User user = new User();
        user.setUsername("test");
        user.setId(order.getUserId());

        when(usersClient.getById(user.getId())).thenReturn(Mono.just(user));

        when(ordersClient.createOrder(any())).thenReturn(Mono.just(order));

        Order createdOrder = client.toBlocking().retrieve(
                HttpRequest.POST("/api/orders", order), Order.class
        );

        assertEquals(order.getId(), createdOrder.getId());
        assertEquals(order.getUserId(), createdOrder.getUserId());
        assertEquals(order.getUserId(), createdOrder.getUser().getId());
        assertEquals(order.getUser().getUsername(), createdOrder.getUser().getUsername());
    }

    @Test
    void createOrderUserDoesntExists() {
        Order order = new Order();
        order.setUserId(2);
        order.setId(1);

        when(ordersClient.createOrder(any())).thenReturn(Mono.just(order));

        when(usersClient.getById(order.getUserId())).thenReturn(Mono.empty());

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> client.toBlocking().retrieve(
                HttpRequest.POST("/api/orders", order), Order.class
        ));

        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);

        assertTrue(exception.getResponse().getBody(String.class).orElse("").contains("User with 2 id doesn't exist"));
    }

    @Test
    void exceptionHandler() {
        User user = new User();
        String message = "Test error message";

        when(usersClient.createUser(any())).thenThrow(new HttpClientResponseException("Test", HttpResponse.badRequest(message)));

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> client.toBlocking().retrieve(
                HttpRequest.POST("/api/users", user), User.class
        ));

        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);

        assertTrue(exception.getResponse().getBody(String.class).orElse("").contains("Test error message"));
    }

}
