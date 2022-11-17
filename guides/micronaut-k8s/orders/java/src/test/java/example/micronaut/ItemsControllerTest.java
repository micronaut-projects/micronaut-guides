package example.micronaut;

import example.micronaut.auth.Credentials;
import example.micronaut.models.Item;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class ItemsControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    Credentials credentials;

    @Test
    void testUnauthorized() {
        HttpClientException exception = assertThrows(HttpClientException.class, () -> client.toBlocking().retrieve(HttpRequest.GET("/items"), HttpStatus.class));
        assertTrue(exception.getMessage().contains("Unauthorized"));
    }

    @Test
    void getUsers() {
        HttpStatus status = client.toBlocking().retrieve(
                HttpRequest.GET("/items")
                        .basicAuth(credentials.getUsername(), credentials.getPassword())
                , HttpStatus.class);
        assertEquals(HttpStatus.OK, status);
    }

    @Test
    void getItem() {

        Integer itemId = 1;

        Item item = client.toBlocking().retrieve(
                HttpRequest.GET("/items/" + itemId)
                        .basicAuth(credentials.getUsername(), credentials.getPassword())
                , Item.class
        );

        assertEquals(itemId, item.getId());
        assertEquals("Banana", item.getName());
        assertEquals(new BigDecimal("1.5"), item.getPrice());

    }

    @Test
    void getItems() {
        HttpResponse<List<Item>> rsp = client.toBlocking().exchange(
                HttpRequest.GET("/items")
                        .basicAuth(credentials.getUsername(), credentials.getPassword()),
                Argument.listOf(Item.class));

        assertEquals(HttpStatus.OK, rsp.getStatus());
        assertNotNull(rsp.body());
        List<Item> items = rsp.body();
        assertNotNull(items);
        List<String> existingItemNames = Arrays.asList("Kiwi", "Banana", "Grape");
        assertEquals(3, items.size());
        assertTrue(items.stream()
                .map(Item::getName)
                .allMatch(name -> existingItemNames.stream().anyMatch(x -> x.equals(name))));
    }

}
