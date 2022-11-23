package example.micronaut;

import example.micronaut.auth.Credentials;
import example.micronaut.models.Order;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class OrdersControllerTest {

    @Inject
    OrderItemClient orderItemClient;

    @Inject
    Credentials credentials;

    @Test
    void testUnauthorized() {
        HttpClientException exception = assertThrows(HttpClientException.class, () -> orderItemClient.getOrders(""));
        assertTrue(exception.getMessage().contains("Unauthorized"));
    }

    @Test
    void multipleOrderInteraction() {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((credentials.getUsername() + ":" + credentials.getPassword()).getBytes());

        Integer userId = 1;
        List<Integer> itemIds = Arrays.asList(1, 1, 2, 3);

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
    void ItemDoesntExists() {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((credentials.getUsername() + ":" + credentials.getPassword()).getBytes());

        Integer userId = 1;
        List<Integer> itemIds = List.of(5);

        Order order = new Order(0, userId, null, itemIds, null);

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> orderItemClient.createOrder(authHeader, order));

        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertTrue(exception.getResponse().getBody(String.class).orElse("").contains("Item with id 5 doesn't exists"));
    }

    @Test
    void OrderEmptyItems() {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((credentials.getUsername() + ":" + credentials.getPassword()).getBytes());

        Integer userId = 1;
        Order order = new Order(0, userId, null, null, null);
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> orderItemClient.createOrder(authHeader, order));

        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertTrue(exception.getResponse().getBody(String.class).orElse("").contains("Items must be supplied"));
    }

}
