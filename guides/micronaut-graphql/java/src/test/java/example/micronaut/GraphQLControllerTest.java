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

@MicronautTest
class GraphQLControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testGraphQLController() {
        String query = "{ \"query\": \"{ bookById(id:\\\"book-1\\\") { name, pageCount, author { firstName, lastName} } }\" }";

        HttpRequest<String> request = HttpRequest.POST("/graphql", query);
        HttpResponse<Map> rsp = client.toBlocking().exchange(request, Argument.of(Map.class));
        assertEquals(HttpStatus.OK, rsp.status());
        assertNotNull(rsp.body());

        Map bookInfo = (Map) rsp.getBody(Map.class).get().get("data");
        Map bookById = (Map) bookInfo.get("bookById");
        assertEquals("Harry Potter and the Philosopher's Stone", bookById.get("name"));
        assertEquals(223, bookById.get("pageCount"));

        Map author = (Map) bookById.get("author");
        assertEquals("Joanne", author.get("firstName"));
        assertEquals("Rowling", author.get("lastName"));
    }
}
