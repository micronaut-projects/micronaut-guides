/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut;

import example.micronaut.auth.Credentials;
import example.micronaut.models.Item;
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
class ItemsControllerTest {

    @Inject
    OrderItemClient orderItemClient;

    @Inject
    Credentials credentials;

    @Test
    void testUnauthorized() {
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> orderItemClient.getItems(""));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
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
