package example.micronaut;

import example.micronaut.clients.OrdersClient;
import example.micronaut.clients.UsersClient;
import example.micronaut.models.Item;
import example.micronaut.models.Order;
import example.micronaut.models.User;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    GatewayClient gatewayClient;

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

        Item retrievedItem = gatewayClient.getItemById(item.id());

        assertEquals(item.id(), retrievedItem.id());
        assertEquals(item.name(), retrievedItem. name());
        assertEquals(item.price(), retrievedItem.price());

    }

    @Test
    void getOrderById() {

        Order order = new Order(1, 2, null, null, new ArrayList<>(), null);
        User user = new User(order.userId(), "firstName", "lastName", "test");

        when(ordersClient.getOrderById(1)).thenReturn(Mono.just(order));
        when(usersClient.getById(user.id())).thenReturn(Mono.just(user));

        Order retrievedOrder = gatewayClient.getOrderById(order.id());

        assertEquals(order.id(), retrievedOrder.id());
        assertEquals(order.userId(), retrievedOrder.user().id());
        assertNull(retrievedOrder.userId());
        assertEquals(user.username(), retrievedOrder.user().username());
    }

    @Test
    void getUserById() {
        User user = new User(1, "firstName", "lastName", "test");

        when(usersClient.getById(1)).thenReturn(Mono.just(user));

        User retrievedUser = gatewayClient.getUsersById(user.id());

        assertEquals(user.id(), retrievedUser.id());
        assertEquals(user.username(), retrievedUser.username());
    }

    @Test
    void getUsers() {
        User user = new User(1, "firstName", "lastName", "test");

        when(usersClient.getUsers()).thenReturn(Flux.just(user));

        List<User> users = gatewayClient.getUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(user.id(), users.get(0).id());
        assertEquals(user.username(), users.get(0).username());
    }

    @Test
    void getItems() {

        Item item = new Item(1, "test", BigDecimal.ONE);

        when(ordersClient.getItems()).thenReturn(Flux.just(item));

        List<Item> items = gatewayClient.getItems();

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(item.name(), items.get(0).name());
        assertEquals(item.price(), items.get(0).price());
    }

    @Test
    void getOrders() {
        Order order = new Order(1, 2, null, null, new ArrayList<>(), null);
        User user = new User(order.userId(), "firstName", "lastName", "test");

        when(ordersClient.getOrders()).thenReturn(Flux.just(order));
        when(usersClient.getById(order.userId())).thenReturn(Mono.just(user));

        List<Order> orders = gatewayClient.getOrders();

        assertNotNull(orders);
        assertEquals(1, orders.size());
        assertNull(orders.get(0).userId());
        assertEquals(user.id(), orders.get(0).user().id());

        assertEquals(order.id(), orders.get(0).id());
        assertEquals(user.username(), orders.get(0).user().username());
    }

    @Test
    void createUser() {
        String firstName = "firstName";
        String lastName = "lastName";
        String username = "username";

        User user = new User(0, firstName, lastName, username);

        when(usersClient.createUser(any())).thenReturn(Mono.just(user));

        User createdUser = gatewayClient.createUser(user);

        assertEquals(firstName, createdUser.firstName());
        assertEquals(lastName, createdUser.lastName());
        assertEquals(username, createdUser.username());
    }

    @Test
    void createOrder() {
        Order order = new Order(1, 2, null, null, new ArrayList<>(), null);
        User user = new User(order.userId(), "firstName", "lastName", "test");

        when(usersClient.getById(user.id())).thenReturn(Mono.just(user));

        when(ordersClient.createOrder(any())).thenReturn(Mono.just(order));

        Order createdOrder = gatewayClient.createOrder(order);

        assertEquals(order.id(), createdOrder.id());
        assertNull(createdOrder.userId());
        assertEquals(order.userId(), createdOrder.user().id());
        assertEquals(user.username(), createdOrder.user().username());
    }

    @Test
    void createOrderUserDoesntExists() {
        Order order = new Order(1, 2, null, null, new ArrayList<>(), new BigDecimal(0));;

        when(ordersClient.createOrder(any())).thenReturn(Mono.just(order));

        when(usersClient.getById(order.userId())).thenReturn(Mono.empty());

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> gatewayClient.createOrder(order));

        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);

        assertTrue(exception.getResponse().getBody(String.class).orElse("").contains("User with 2 id doesn't exist"));
    }

    @Test
    void exceptionHandler() {
        User user = new User(1, "firstname", "lastname", "username");

        String message = "Test error message";

        when(usersClient.createUser(any())).thenThrow(new HttpClientResponseException("Test", HttpResponse.badRequest(message)));

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> gatewayClient.createUser(user));

        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);

        assertTrue(exception.getResponse().getBody(String.class).orElse("").contains("Test error message"));
    }

}
