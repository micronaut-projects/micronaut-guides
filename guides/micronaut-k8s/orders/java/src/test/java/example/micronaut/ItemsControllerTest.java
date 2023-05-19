package example.micronaut;

import example.micronaut.auth.Credentials;
import example.micronaut.models.Item;
import io.micronaut.http.client.exceptions.HttpClientException;
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

@MicronautTest // <1>
class ItemsControllerTest {

    @Inject
    OrderItemClient orderItemClient;

    @Inject
    Credentials credentials;

    @Test
    void testUnauthorized() {
        HttpClientException exception = assertThrows(HttpClientException.class, () -> orderItemClient.getItems(""));
        assertTrue(exception.getMessage().contains("Unauthorized"));
    }

    @Test
    void getItem() {

        int itemId = 1;

        String authHeader = basicAuth(credentials);

        Item item = orderItemClient.getItemsById(authHeader, itemId);

        assertEquals(itemId, item.id());
        assertEquals("Banana", item.name());
        assertEquals(new BigDecimal("1.5"), item.price());
    }

    @Test
    void getItems() {
        String authHeader = basicAuth(credentials);

        List<Item> items = orderItemClient.getItems(authHeader);

        assertNotNull(items);
        List<String> existingItemNames = List.of("Kiwi", "Banana", "Grape");
        assertEquals(3, items.size());
        assertTrue(items.stream()
                .map(Item::name)
                .allMatch(name -> existingItemNames.stream().anyMatch(x -> x.equals(name))));
    }

    private static String basicAuth(Credentials credentials) {
        return basicAuth(credentials.username(), credentials.password());
    }
    private static String basicAuth(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
