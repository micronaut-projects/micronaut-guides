/*
 * Copyright 2017-2026 original authors
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

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest // <1>
class ProductsTest {

    @Test
    void theAppServesTheUi(@Client("/") HttpClient httpClient) { // <2>
        BlockingHttpClient client = httpClient.toBlocking();
        String html = client.retrieve(HttpRequest.GET("/products").accept(MediaType.TEXT_HTML));
        assertTrue(html.contains("<mateu-ui")); // <3>
    }

    @Test
    void theListingShowsTheProducts(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        Map<String, Object> search = Map.of(
                "route", "/products",
                "consumedRoute", "/products",
                "serverSideType", Products.class.getName(),
                "actionId", "search",
                "componentState", Map.of("searchText", "", "page", 0, "size", 10));
        String json = client.retrieve(HttpRequest.POST("/mateu/v3/sync/products", search)); // <4>
        assertTrue(json.contains("Espresso machine"));
        assertTrue(json.contains("Pour-over kettle"));
    }
}
