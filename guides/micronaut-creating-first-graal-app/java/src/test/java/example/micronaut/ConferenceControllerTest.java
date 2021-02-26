package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class ConferenceControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void testHello() throws Exception {
        HttpRequest request = HttpRequest.GET("/conferences/random");
        String body = client.toBlocking().retrieve(request);
        assertNotNull(body);
    }
}
