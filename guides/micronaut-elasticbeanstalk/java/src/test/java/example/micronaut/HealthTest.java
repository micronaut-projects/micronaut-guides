package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import jakarta.inject.Inject;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest // <1>
public class HealthTest {

    @Inject
    @Client("/")
    HttpClient client; // <2>

    @Test
    public void testHealthRespondsOK()  {
        Map m = client.toBlocking().retrieve(HttpRequest.GET("/health"), Map.class); // <3>

        assertNotNull(m);
        assertTrue(m.containsKey("status"));
        assertEquals(m.get("status"), "UP");
    }
}