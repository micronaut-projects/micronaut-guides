package example.micronaut;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@MicronautTest // <1>
public class HelloControllerTest {

    @Inject
    @Client("/")  // <2>
    HttpClient client;

    @Test
    public void testHello() {
        String body = client.toBlocking().retrieve(HttpRequest.GET("/")); // <3>
        assertNotNull(body);
        assertEquals("{\"message\":\"Hello World\"}", body);
    }
}