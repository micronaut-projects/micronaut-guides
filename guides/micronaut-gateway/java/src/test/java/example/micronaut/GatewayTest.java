package example.micronaut;

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GatewayTest {

    @Test
    void gateway() {
        EmbeddedServer grailsServer = ApplicationContext.run(EmbeddedServer.class, Collections.singletonMap("framework", "grails"));
        HttpClient grailsHttpClient = grailsServer.getApplicationContext().createBean(HttpClient.class, grailsServer.getURL());
        BlockingHttpClient grailsClient = grailsHttpClient.toBlocking();

        EmbeddedServer micronautServer = ApplicationContext.run(EmbeddedServer.class, Collections.singletonMap("framework", "micronaut"));
        HttpClient micronautHttpClient = micronautServer.getApplicationContext().createBean(HttpClient.class, micronautServer.getURL());
        BlockingHttpClient micronautClient = micronautHttpClient.toBlocking();

        Map<String, Object> configuration = Map.of(
                "micronaut.gateway.routes.micronaut.uri", micronautServer.getURL().toString(),
                "micronaut.gateway.routes.micronaut.predicates[0].path", "/micronaut/books",
                "micronaut.gateway.routes.grails.uri", grailsServer.getURL().toString(),
                "micronaut.gateway.routes.grails.predicates[0].path", "/grails/books"
        );
        EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class, configuration);
        HttpClient httpClient = server.getApplicationContext().createBean(HttpClient.class, server.getURL());
        BlockingHttpClient client = httpClient.toBlocking();

        HttpRequest<?> micronautRequest = HttpRequest.GET("/micronaut/books");
        HttpRequest<?> grailsRequest = HttpRequest.GET("/grails/books");
        List<Book> books = assertDoesNotThrow(() -> grailsClient.retrieve(grailsRequest, Argument.listOf(Book.class)));
        assertEquals(6, books.size());

        books = assertDoesNotThrow(() -> micronautClient.retrieve(micronautRequest, Argument.listOf(Book.class)));
        assertEquals(2, books.size());


        books = assertDoesNotThrow(() -> client.retrieve(micronautRequest, Argument.listOf(Book.class)));
        assertEquals(2, books.size());

        books = assertDoesNotThrow(() -> client.retrieve(grailsRequest, Argument.listOf(Book.class)));
        assertEquals(6, books.size());

        grailsServer.close();
        micronautServer.close();

        assertThrows(HttpClientException.class, () -> client.exchange(micronautRequest));

        assertThrows(HttpClientException.class, () -> client.exchange(grailsRequest));

        server.close();
    }
}
