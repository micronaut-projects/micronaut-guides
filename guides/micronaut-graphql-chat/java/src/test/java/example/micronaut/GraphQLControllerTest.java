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

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
@SuppressWarnings("unchecked")
class GraphQLControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void queryAndMutateMessages() {
        Map<String, Object> initial = execute("{ messages { from text } }");
        List<Map<String, Object>> messages = (List<Map<String, Object>>) ((Map<String, Object>) initial.get("data")).get("messages");
        assertEquals(0, messages.size());

        Map<String, Object> created = execute("mutation { chat(from:\\\"Ada\\\", text:\\\"Hello GraphQL\\\") { from text } }");
        Map<String, Object> createdMessage = (Map<String, Object>) ((Map<String, Object>) created.get("data")).get("chat");
        assertEquals("Ada", createdMessage.get("from"));
        assertEquals("Hello GraphQL", createdMessage.get("text"));

        Map<String, Object> queried = execute("{ messages { from text } }");
        List<Map<String, Object>> updatedMessages = (List<Map<String, Object>>) ((Map<String, Object>) queried.get("data")).get("messages");
        assertEquals(List.of(Map.of("from", "Ada", "text", "Hello GraphQL")), updatedMessages);
    }

    private Map<String, Object> execute(String graphql) {
        String query = """
        { "query": "%s" }
        """.formatted(graphql);

        HttpResponse<Map<String, Object>> response = client.toBlocking().exchange(
                HttpRequest.POST("/graphql", query),
                Argument.mapOf(String.class, Object.class)
        );
        assertEquals(HttpStatus.OK, response.status());
        return response.body();
    }
}
