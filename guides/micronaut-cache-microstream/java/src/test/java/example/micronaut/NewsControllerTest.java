package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

import java.time.Month;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // <1>
@MicronautTest // <2>
class NewsControllerTest {

    @Inject
    @Client("/") // <3>
    HttpClient client;

    static String expected = "Micronaut AOP: Awesome flexibility without the complexity";

    @Timeout(30) // <4>
    @Order(1) // <5>
    @Test
    void fetchingOctoberHeadlinesFirstTimeDoesNotUseCache() {
        News news = client.toBlocking().retrieve(createRequest(), News.class);
        assertEquals(Arrays.asList(expected), news.getHeadlines());
    }

    @Timeout(1) // <4>
    @Order(2) // <5>
    @Test
    void fetchingOctoberHeadlinesSecondTimeUsesCache() {
        News news = client.toBlocking().retrieve(createRequest(), News.class);
        assertEquals(Arrays.asList(expected), news.getHeadlines());
    }

    private static HttpRequest<?> createRequest() {
        return HttpRequest.GET(UriBuilder.of("/")
                .path(Month.OCTOBER.name())
                .build());
    }
}
