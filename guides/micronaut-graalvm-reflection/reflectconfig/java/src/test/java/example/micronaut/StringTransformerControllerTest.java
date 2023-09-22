package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest // <1>
class StringTransformerControllerTest {

    @Test
    void capitalize(@Client("/") HttpClient httpClient) { // <2>
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = uri("capitalize");
        assertEquals("HELLO", client.retrieve(HttpRequest.GET(uri)));
    }

    @Test
    void reverse(@Client("/") HttpClient httpClient) { // <2>
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = uri("reverse");
        assertEquals("olleh", client.retrieve(HttpRequest.GET(uri)));
    }

    private URI uri(String path) {
        return UriBuilder.of("/transformer") // <3>
                .path(path)
                .queryParam("q", "hello")
                .build();
    }

}