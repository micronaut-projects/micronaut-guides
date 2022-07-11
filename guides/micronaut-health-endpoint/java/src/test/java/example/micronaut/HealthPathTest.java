package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import io.micronaut.context.annotation.Property;
import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Property(name = "endpoints.all.path", value = "/endpoints")
@MicronautTest
public class HealthPathTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void healthEndpointExposed() {
        HttpStatus status = client.toBlocking().retrieve(HttpRequest.GET("/endpoints/health"), HttpStatus.class);
        assertEquals(HttpStatus.OK, status);

        Executable e = () -> client.toBlocking().retrieve(HttpRequest.GET("/health"), HttpStatus.class);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, e);
    }
}
