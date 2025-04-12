package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Property(name = "greeting.name", value = "Dakota")
@Property(name = "greeting.coffee", value = "Dakota is drinking Café Cereza")
@MicronautTest
class GreetingConfigurationPropertiesControllerTest {

    @Test
    void defaultValue(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = UriBuilder.of("/greet").path("name").build();

        String txt = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET(uri).accept(MediaType.TEXT_PLAIN)));
        assertEquals("Dakota", txt);
    }

    @Test
    void derivedProperties(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = UriBuilder.of("/greet").path("coffee").build();
        String txt = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET(uri).accept(MediaType.TEXT_PLAIN)));
        assertEquals("Dakota is drinking Café Cereza", txt);
    }
}