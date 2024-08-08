package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest // <1>
public class PygalControllerTest {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")  // <2>
    HttpClient client;

    @Test
    void testPygalResponse() {
        String response = client.toBlocking().retrieve(HttpRequest.GET("/pygal")); // <3>
        assertTrue(response.contains("<svg xmlns:xlink"));
        assertTrue(response.contains("<title>Pygal</title>"));
        assertTrue(response.contains("<g class=\"graph stackedbar-graph vertical\">"));
    }

}
