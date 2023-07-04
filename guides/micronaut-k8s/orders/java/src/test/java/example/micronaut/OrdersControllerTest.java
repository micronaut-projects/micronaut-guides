package example.micronaut;

import example.micronaut.auth.Credentials;
import example.micronaut.models.Order;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest // <1>
class OrdersControllerTest {

    @Inject
    OrderItemClient orderItemClient;

    @Inject
    Credentials credentials;

    @Test
    void testUnauthorized() {
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> orderItemClient.getOrders(""));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void multipleOrderInteraction() {
        String authHeader = basicAuth(credentials);

        int userId = 1;
        List<Integer> itemIds = List.of(1, 1, 2, 3);

        Order order = new Order(0, userId, null, itemIds, null);

        Order createdOrder = orderItemClient.createOrder(authHeader, order);

        assertNotNull(createdOrder.items());

        assertEquals(4, createdOrder.items().size());
        assertEquals(new BigDecimal("6.75"), createdOrder.total());
        assertEquals(userId, createdOrder.userId());

        Order retrievedOrder = orderItemClient.getOrderById(authHeader, createdOrder.id());

        assertNotNull(retrievedOrder.items());

        assertEquals(4, retrievedOrder.items().size());
        assertEquals(new BigDecimal("6.75"), retrievedOrder.total());
        assertEquals(userId, retrievedOrder.userId());

        List<Order> orders = orderItemClient.getOrders(authHeader);

        assertNotNull(orders);
        assertTrue(orders.stream()
                .map(Order::userId)
                .anyMatch(id -> id.equals(userId)));

    }

    @Test
    void itemDoesntExists() {
        String authHeader = basicAuth(credentials);

        int userId = 1;
        List<Integer> itemIds = List.of(5);

        Order order = new Order(0, userId, null, itemIds, null);

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> orderItemClient.createOrder(authHeader, order));

        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());

        assertTrue(exception.getResponse().getBody(String.class).orElse("").contains("Item with id 5 doesn't exist"));
    }

    @Test
    void orderEmptyItems() {
        String authHeader = basicAuth(credentials);

        int userId = 1;
        Order order = new Order(0, userId, null, null, null);
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> orderItemClient.createOrder(authHeader, order));

        assertEquals(HttpStatus.BAD_REQUEST,exception.getStatus());
        assertTrue(exception.getResponse().getBody(String.class).orElse("").contains("Items must be supplied"));
    }


    private static String basicAuth(Credentials credentials) {
        return basicAuth(credentials.username(), credentials.password());
    }
    private static String basicAuth(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

}
