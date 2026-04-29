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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
@SuppressWarnings("unchecked")
class GraphQLControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void queryAndMutateToDos() {
        Map<String, Object> initial = execute("{ toDos { id title completed } }");
        List<Map<String, Object>> toDos = (List<Map<String, Object>>) ((Map<String, Object>) initial.get("data")).get("toDos");
        assertEquals(3, toDos.size());

        Map<String, Object> created = execute("mutation { createToDo(title:\\\"Write the SPQR guide\\\") { id title completed } }");
        Map<String, Object> createdToDo = (Map<String, Object>) ((Map<String, Object>) created.get("data")).get("createToDo");
        assertEquals("Write the SPQR guide", createdToDo.get("title"));
        assertEquals(Boolean.FALSE, createdToDo.get("completed"));
        assertNotNull(createdToDo.get("id"));

        Map<String, Object> completed = execute("mutation { completeToDo(id:\\\"" + createdToDo.get("id") + "\\\") }");
        assertTrue((Boolean) ((Map<String, Object>) completed.get("data")).get("completeToDo"));

        Map<String, Object> deleted = execute("mutation { deleteToDo(id:\\\"" + createdToDo.get("id") + "\\\") }");
        assertTrue((Boolean) ((Map<String, Object>) deleted.get("data")).get("deleteToDo"));

        Map<String, Object> missing = execute("mutation { deleteToDo(id:\\\"missing\\\") }");
        assertFalse((Boolean) ((Map<String, Object>) missing.get("data")).get("deleteToDo"));
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
