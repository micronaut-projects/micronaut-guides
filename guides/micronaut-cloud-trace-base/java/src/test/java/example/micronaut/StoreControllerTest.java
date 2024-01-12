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

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;


import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class StoreControllerTest {

    @Inject
    @Client("/store")
    HttpClient client;

    @Test
    public void testInventoryItem() {
        HttpRequest<String> request = HttpRequest.GET("/inventory/laptop");
        HashMap<String, ?> inventory = client.toBlocking().retrieve(request, HashMap.class);

        assertNotNull(inventory);
        assertEquals("laptop", inventory.get("item"));
        assertEquals(4, inventory.get("store"));
        assertNotNull(inventory.get("warehouse"));
    }

    @Test
    public void testInventoryItemNotFound() {

        HashMap inventory = client.toBlocking().retrieve("/inventory/chair", HashMap.class);

        assertNotNull(inventory);
        assertEquals("chair", inventory.get("item"));
        assertEquals("Not available at store", inventory.get("note"));
    }

    @Test
    public void testInventoryAll() {
        HttpRequest request = HttpRequest.GET("/inventory");
        List<HashMap> inventory = client.toBlocking().retrieve(request, Argument.listOf(HashMap.class));

        assertNotNull(inventory);
        assertEquals(3, inventory.size());

        List<String> names = inventory.stream().map(it -> (String) it.get("item")).collect(Collectors.toList());
        assertTrue(names.containsAll(List.of("desktop", "monitor", "laptop")));
    }

    @Test
    public void testOrder() {
        Map<String, ?> item = Map.of("item", "desktop", "count", 8);
        HttpRequest request = HttpRequest.POST("/order", item);
        HttpResponse response = client.toBlocking().exchange(request);

        assertEquals(HttpStatus.CREATED, response.getStatus());

        HashMap<String, ?> inventory = client.toBlocking().retrieve("/inventory/desktop", HashMap.class);

        assertNotNull(inventory);
        assertEquals("desktop", inventory.get("item"));
        assertEquals(10, inventory.get("store"));
        assertNull(inventory.get("warehouse"));
    }

}
