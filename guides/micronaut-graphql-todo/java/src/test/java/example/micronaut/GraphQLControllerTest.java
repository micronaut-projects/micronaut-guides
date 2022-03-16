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

@MicronautTest // <1>
class GraphQLControllerTest {

    @Inject
    @Client("/")
    HttpClient client; // <2>

    @Test
    void testGraphQLController() {
        // when:
        List<Map> todos = getTodos();

        // then:
        assertTrue(todos.isEmpty());

        // when:
        Long id = createToDo("Test GraphQL", "Tim Yates");

        // then: (check it's a UUID)
        assertEquals(1, id);

        // when:
        todos = getTodos();

        // then:
        assertEquals(1, todos.size());

        Map<?,?> todo = todos.get(0);

        assertEquals("Test GraphQL", todo.get("title"));
        assertFalse(Boolean.parseBoolean(todo.get("completed").toString()));
        assertEquals("Tim Yates", ((Map)todo.get("author")).get("username"));

        // when:
        Boolean completed = markAsCompleted(id);

        // then:
        assertTrue(completed);

        // when:
        todos = getTodos();

        // then:
        assertEquals(1, todos.size());
        todo = todos.get(0);
        assertEquals("Test GraphQL", todo.get("title"));
        assertTrue(Boolean.parseBoolean(todo.get("completed").toString()));
        assertEquals("Tim Yates", ((Map)todo.get("author")).get("username"));
    }

    private HttpResponse<Map> fetch(String query) {
        HttpRequest<String> request = HttpRequest.POST("/graphql", query);
        HttpResponse<Map> response = client.toBlocking().exchange(request, Argument.of(Map.class));
        assertEquals(HttpStatus.OK, response.status());
        assertNotNull(response.body());
        return response;
    }

    private List<Map> getTodos() {
        String query = "{\"query\":\"query { toDos { title, completed, author { id, username } } }\"}";
        HttpResponse<Map> response = fetch(query);
        return (List<Map>) ((Map) response.getBody().get().get("data")).get("toDos");
    }

    private long createToDo(String title, String author) {
        String query = "{\"query\": \"mutation { createToDo(title: \\\"" + title + "\\\", author: \\\"" + author + "\\\") { id } }\" }";
        HttpResponse<Map> response = fetch(query);
        return Long.parseLong(((Map)((Map) response.getBody(Map.class).get().get("data")).get("createToDo")).get("id").toString());
    }

    private Boolean markAsCompleted(Long id) {
        String query = "{\"query\": \"mutation { completeToDo(id: \\\"" + id + "\\\") }\" }";
        HttpResponse<Map> response = fetch(query);
        return (Boolean) ((Map) response.getBody(Map.class).get().get("data")).get("completeToDo");
    }
}
