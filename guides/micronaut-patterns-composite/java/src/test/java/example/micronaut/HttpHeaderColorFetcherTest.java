package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Property(name = "spec.name", value = "HttpHeaderColorFetcherTest") // <1>
@MicronautTest // <2>
class HttpHeaderColorFetcherTest {
    @Inject
    @Client("/")
    HttpClient httpClient; // <3>

    @Test
    void theHttpHeaderColorFetcherFetchesFromColorHeader() {
        BlockingHttpClient client = httpClient.toBlocking();
        assertEquals("yellow", client.retrieve(HttpRequest.GET("/header/color").header("color", "yellow")));
        assertThrows(HttpClientResponseException.class, () -> client.retrieve(HttpRequest.GET("/header/color")));
    }

    @Requires(property = "spec.name", value = "HttpHeaderColorFetcherTest") // <1>
    @Controller("/header")
    static class HttpHeaderColorFetcherTestController {
        private final HttpHeaderColorFetcher colorFetcher;

        HttpHeaderColorFetcherTestController(HttpHeaderColorFetcher colorFetcher) {
            this.colorFetcher = colorFetcher;
        }

        @Produces(MediaType.TEXT_PLAIN)
        @Get("/color")
        Optional<String> index(HttpRequest<?> request) {
            return colorFetcher.favouriteColor(request);
        }
    }


}