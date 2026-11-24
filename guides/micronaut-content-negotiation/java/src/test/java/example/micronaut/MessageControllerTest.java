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

import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest // <1>
class MessageControllerTest {

    @Test
    void contentNegotiation(@Client("/") HttpClient httpClient) { // <2>
        BlockingHttpClient client = httpClient.toBlocking();

        String expectedJson = """
                {"message":"Hello World"}""";
        String json = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET("/")
                .accept(MediaType.APPLICATION_JSON))); // <3>
        assertEquals(expectedJson, json);

        String expectedHtml = """
                <!DOCTYPE html>
                <html lang="en">
                <body>
                <h1>Hello World</h1>
                </body>
                </html>
                """;
        String html = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET("/")
                .accept(MediaType.TEXT_HTML)));
        assertEquals(expectedHtml, html);
    }

    @Test
    void prefersJsonWhenItHasTheHigherQuality(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();

        HttpResponse<String> response = assertDoesNotThrow(() -> client.exchange(HttpRequest.GET("/")
                .header(HttpHeaders.ACCEPT, "text/html;q=0.1, application/json;q=0.9"), String.class));
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(Optional.of(MediaType.APPLICATION_JSON_TYPE), response.getContentType());
        assertEquals("""
                {"message":"Hello World"}""", response.body());
    }

    @Test
    void jsonIsTheDefaultWithoutAnAcceptHeader(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();

        HttpResponse<String> response = assertDoesNotThrow(() -> client.exchange(HttpRequest.GET("/"), String.class));
        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(Optional.of(MediaType.APPLICATION_JSON_TYPE), response.getContentType());
        assertEquals("""
                {"message":"Hello World"}""", response.body());
    }
}