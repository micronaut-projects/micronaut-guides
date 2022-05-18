package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest // <1>
class ColorControllerTest {

    @Inject
    @Client("/")
    HttpClient httpClient; // <2>

    @Test
    void testCompositePattern() {
        BlockingHttpClient client = httpClient.toBlocking();
        assertEquals("yellow", client.retrieve(HttpRequest.GET("/color").header("color", "yellow")));
        assertThrows(HttpClientResponseException.class, () -> client.retrieve(HttpRequest.GET("/color")));

        assertEquals("yellow", client.retrieve(HttpRequest.GET("/color/mint").header("color", "yellow")));
        assertEquals("mint", client.retrieve(HttpRequest.GET("/color/mint")));
    }
}
