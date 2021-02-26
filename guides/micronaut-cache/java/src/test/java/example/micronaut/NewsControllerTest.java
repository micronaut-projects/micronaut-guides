package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import javax.inject.Inject;
import java.time.Month;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest
public class NewsControllerTest {

    @Inject
    EmbeddedServer server;

    @Inject
    @Client("/")
    HttpClient client;

    @Timeout(4) // <1>
    @Test
    void fetchingOctoberHeadlinesUsesCache() {
        HttpRequest request = HttpRequest.GET(UriBuilder.of("/").path(Month.OCTOBER.toString()).build());
        News news = client.toBlocking().retrieve(request, News.class);
        String expected = "Micronaut AOP: Awesome flexibility without the complexity";
        assertEquals(Arrays.asList(expected), news.getHeadlines());

        news = client.toBlocking().retrieve(request, News.class);
        assertEquals(Arrays.asList(expected), news.getHeadlines());
    }
}
