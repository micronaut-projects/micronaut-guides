package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest // <1>
public class HelloControllerTest {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")  // <2>
    HttpClient client;

    @Test
    void testHelloResponse() {
        String response = client.toBlocking().retrieve(HttpRequest.GET("/hello")); // <3>
        assertEquals("Hello World", response);
    }

}
