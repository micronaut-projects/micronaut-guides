package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
class ConferenceControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testHello() {
        HttpRequest<?> request = HttpRequest.GET("/conferences/random");
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
    }
}
