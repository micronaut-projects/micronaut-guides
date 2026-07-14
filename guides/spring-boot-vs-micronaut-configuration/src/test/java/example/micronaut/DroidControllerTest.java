package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "droid.id", value = "BB-8")
@Property(name = "droid.description", value = "Small, rolling android. Probably does not drink coffee")
@MicronautTest
class DroidControllerTest {

    @Disabled
    @Test
    void droidViaFactory(@Client("/")HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = HttpRequest.GET("/droid");
        Droid droid = assertDoesNotThrow(() -> client.retrieve(request, Droid.class));
        assertNotNull(droid);
        assertEquals("BB-8", droid.getId());
        assertEquals("Small, rolling android. Probably does not drink coffee", droid.getDescription());
    }
}