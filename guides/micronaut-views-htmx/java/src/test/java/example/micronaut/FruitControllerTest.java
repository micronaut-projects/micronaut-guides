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

import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.views.htmx.http.HtmxRequestHeaders;
import io.micronaut.views.htmx.http.HtmxResponseHeaders;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest // <1>
class FruitControllerTest {

    @Test
    void normalRequestRendersFullPage(@Client("/") HttpClient httpClient) { // <2>
        BlockingHttpClient client = httpClient.toBlocking();

        String html = client.retrieve("/fruits");

        assertTrue(html.contains("<!DOCTYPE html>"));
        assertTrue(html.contains("hx-post=\"/fruits\""));
        assertTrue(html.contains("<h2>Apple</h2>"));
    }

    @Test
    void htmxRequestRendersPartial(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET("/fruits")
                .header(HtmxRequestHeaders.HX_REQUEST, StringUtils.TRUE);

        String html = client.retrieve(request);

        assertFalse(html.contains("<!DOCTYPE html>"));
        assertTrue(html.contains("<section id=\"fruit\">"));
        assertTrue(html.contains("<h2>Apple</h2>"));
    }

    @Test
    void htmxPostCanRenderOutOfBandSwap(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.POST("/fruits", Collections.emptyMap())
                .header(HtmxRequestHeaders.HX_REQUEST, StringUtils.TRUE);

        String html = client.retrieve(request);

        assertTrue(html.contains("<h2>Banana</h2>"));
        assertTrue(html.contains("<div id=\"message\" hx-swap-oob=\"true\">Selected Banana</div>"));
    }

    @Test
    void htmxResponseHeadersCanBeSet(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET("/fruits/refresh")
                .header(HtmxRequestHeaders.HX_REQUEST, StringUtils.TRUE);

        HttpResponse<?> response = client.exchange(request);

        assertEquals(StringUtils.TRUE, response.getHeaders().get(HtmxResponseHeaders.HX_REFRESH));
    }
}
