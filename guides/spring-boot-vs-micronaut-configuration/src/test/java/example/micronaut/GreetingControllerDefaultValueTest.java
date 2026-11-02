package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
class GreetingControllerDefaultValueTest {

    @Test
    void defaultValue(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        String txt = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET("/greeting").accept(MediaType.TEXT_PLAIN)));
        assertEquals("Mirage", txt);
    }

    @Disabled
    @Test
    void derivedProperties(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = UriBuilder.of("/greeting").path("coffee").build();
        String txt = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET(uri).accept(MediaType.TEXT_PLAIN)));
        assertEquals("Mirage is drinking Café Ganador", txt);
    }
}
