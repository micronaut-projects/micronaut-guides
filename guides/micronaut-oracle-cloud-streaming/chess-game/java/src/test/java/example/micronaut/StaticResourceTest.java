package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;

@MicronautTest
public class StaticResourceTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void staticResourcesAreExposedAtPublic() {
        HttpResponse<?> response = client.toBlocking().exchange("/index.html");
        assertEquals(HttpStatus.OK, response.status());
    }
}
