package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <1>
@MicronautTest // <2>
class BookControllerTest extends AbstractTest { // <3>

    @Test
    void bookUpsert(BookRepository repository, // <4>
                    @Client("/") HttpClient httpClient) { // <5>
        BlockingHttpClient client = httpClient.toBlocking();
        String path = "/books";
        String title = "Netty in Action";
        assertNull(repository.findByTitle(title)); // <6>
        HttpRequest<?> postRequest = HttpRequest.POST(path, Map.of("id", 123L, "title", title));
        HttpResponse<?> postResponse = assertDoesNotThrow(() -> client.exchange(postRequest));
        assertEquals(HttpStatus.CREATED, postResponse.getStatus()); // <7>
        assertNotNull(repository.findByTitle(title));
        assertNull(repository.findByTitle(title).pages());

        HttpRequest<?> putRequest = HttpRequest.PUT(path, Map.of("id", 123L, "title", title, "pages", 265));
        HttpResponse<?> putResponse = assertDoesNotThrow(() -> client.exchange(putRequest));
        assertEquals(HttpStatus.OK, putResponse.getStatus()); // <8>
        assertNotNull(repository.findByTitle(title).pages()); // <9>
    }

}