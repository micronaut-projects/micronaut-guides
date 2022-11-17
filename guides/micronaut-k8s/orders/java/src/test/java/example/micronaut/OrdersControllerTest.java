package example.micronaut;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import example.micronaut.auth.Credentials;
import example.micronaut.models.Item;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.Max;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class OrdersControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    Credentials credentials;

    @Test
    void testUnauthorized() {
        HttpClientException exception = assertThrows(HttpClientException.class, () -> client.toBlocking().retrieve(HttpRequest.GET("/orders"), HttpStatus.class));
        assertTrue(exception.getMessage().contains("Unauthorized"));
    }

    @Test
    void getUsers() {
        HttpStatus status = client.toBlocking().retrieve(
                HttpRequest.GET("/orders")
                        .basicAuth(credentials.getUsername(), credentials.getPassword())
                , HttpStatus.class);
        assertEquals(HttpStatus.OK, status);
    }

    @Test
    void multipleOrderInteraction() {
        Order order = new Order();
        Integer userId = 1;
        List<Integer> itemIds = Arrays.asList(1, 1, 2, 3);
        order.setUserId(userId);
        order.setItemIds(itemIds);

        Order createdOrder = client.toBlocking().retrieve(
                HttpRequest.POST("/orders", order)
                        .basicAuth(
                                credentials.getUsername(), credentials.getPassword()
                        ), Order.class
        );

        assertNotNull(createdOrder.getItems());

        assertEquals(4, createdOrder.getItems().size());
        assertEquals(new BigDecimal("6.75"), createdOrder.getTotal());
        assertEquals(userId, createdOrder.getUserId());

        Order retrievedOrder = client.toBlocking().retrieve(
                HttpRequest.GET("/orders/" + createdOrder.getId())
                        .basicAuth(credentials.getUsername(), credentials.getPassword())
                , Order.class
        );

        assertNotNull(retrievedOrder.getItems());

        assertEquals(4, retrievedOrder.getItems().size());
        assertEquals(new BigDecimal("6.75"), retrievedOrder.getTotal());
        assertEquals(userId, retrievedOrder.getUserId());

        HttpResponse<List<Order>> rsp = client.toBlocking().exchange(
                HttpRequest.GET("/orders")
                        .basicAuth(credentials.getUsername(), credentials.getPassword()),
                Argument.listOf(Order.class));

        assertEquals(HttpStatus.OK, rsp.getStatus());
        assertNotNull(rsp.body());
        List<Order> users = rsp.body();
        assertNotNull(users);
        assertTrue(users.stream()
                .map(Order::getUserId)
                .anyMatch(id -> id.equals(userId)));

    }

    @Test
    void ItemDoesntExists() {
        Order order = new Order();
        Integer userId = 1;
        List<Integer> itemIds = List.of(5);
        order.setUserId(userId);
        order.setItemIds(itemIds);
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> client.toBlocking().retrieve(
                HttpRequest.POST("/orders", order)
                        .basicAuth(
                                credentials.getUsername(), credentials.getPassword()
                        ), Order.class
        ));

        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertTrue(exception.getResponse().getBody(String.class).orElse("").contains("Item with id 5 doesn't exists"));
    }

    @Test
    void OrderEmptyItems() {
        Order order = new Order();
        Integer userId = 1;
        order.setUserId(userId);
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> client.toBlocking().retrieve(
                HttpRequest.POST("/orders", order)
                        .basicAuth(
                                credentials.getUsername(), credentials.getPassword()
                        ), Order.class
        ));

        assertEquals(exception.getStatus(), HttpStatus.BAD_REQUEST);
        assertTrue(exception.getResponse().getBody(String.class).orElse("").contains("Items must be supplied"));
    }

    @Introspected
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Order {

        @Max(10000)
        private Integer id;

        @JsonProperty("user_id")
        private Integer userId;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private List<Item> items = new ArrayList<>();

        @JsonProperty(value = "items_id", access = JsonProperty.Access.READ_ONLY)
        private List<Integer> itemIds = new ArrayList<>();
        private BigDecimal total;

        public List<Integer> getItemIds() {
            return itemIds;
        }

        public void setItemIds(List<Integer> itemIds) {
            this.itemIds = itemIds;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public void setTotal(BigDecimal total) {
            this.total = total;
        }
    }

}
