package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest // <1>
class ControlPanelTest {

    @Inject
    @Client("/") // <2>
    HttpClient client;

    @Test
    void rendersTheControlPanel() {
        var blockingClient = client.toBlocking();
        var index = blockingClient.exchange(HttpRequest.GET("/control-panel"), String.class);

        assertEquals(HttpStatus.OK, index.status());
        assertTrue(index.body().contains("Routes"));
    }
}
