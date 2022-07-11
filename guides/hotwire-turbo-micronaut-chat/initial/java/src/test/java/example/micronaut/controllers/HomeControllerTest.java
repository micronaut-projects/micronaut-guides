package example.micronaut.controllers;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@MicronautTest
class HomeControllerTest {
    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void rootRedirectsToRoomControllerIndex() {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<?> response = client.exchange(HttpRequest.GET("/"));
        assertEquals(HttpStatus.SEE_OTHER, response.status());
        assertEquals("/rooms", response.getHeaders().get(HttpHeaders.LOCATION));
    }
}