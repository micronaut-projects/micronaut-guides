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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
@SuppressWarnings("unchecked")
class GraphQLControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testGraphQLController() {
        Map<String, Object> body = makeRequest("book-1");
        assertNotNull(body);

        Map<String, Object> bookInfo = (Map<String, Object>) body.get("data");
        assertTrue(bookInfo.containsKey("bookById"));

        Map<String, Object> bookById = (Map<String, Object>) bookInfo.get("bookById");

        assertEquals("Harry Potter and the Philosopher's Stone", bookById.get("name"));
        assertEquals(223, bookById.get("pageCount"));

        Map<String, Object> author = (Map<String, Object>) bookById.get("author");
        assertEquals("Joanne", author.get("firstName"));
        assertEquals("Rowling", author.get("lastName"));
    }

    @Test
    void testGraphQLControllerEmptyResponse() {
        Map<String, Object> body = makeRequest("missing-id");
        assertNotNull(body);

        Map<String, Object> bookInfo = (Map<String, Object>) body.get("data");
        assertTrue(bookInfo.containsKey("bookById"));

        Map<String, Object> bookById = (Map<String, Object>) bookInfo.get("bookById");
        assertNull(bookById);
    }

    private Map<String, Object> makeRequest(String id) {
        String query = """
        { "query": "{ bookById(id:\\"%s\\") { name, pageCount, author { firstName, lastName} } }" }""".formatted(id);

        HttpRequest<String> request = HttpRequest.POST("/graphql", query);
        HttpResponse<Map<String, Object>> rsp = client.toBlocking().exchange(request, Argument.mapOf(String.class, Object.class));
        assertEquals(HttpStatus.OK, rsp.status());
        return rsp.body();
    }
}
