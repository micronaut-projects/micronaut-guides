package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import jakarta.inject.Inject;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // <1>
@MicronautTest // <2>
@TestInstance(PER_CLASS) // <3>
public class NewsControllerTest implements TestPropertyProvider { // <4>

    @Override
    @NonNull
    public Map<String, String> getProperties() { // <4>
        return Collections.singletonMap("hazelcast.client.network.addresses",
                HazelcastUtils.getUrl());
    }

    @Inject
    @Client("/") // <5>
    HttpClient client;

    static String expected = "Micronaut AOP: Awesome flexibility without the complexity";

    @Timeout(30) // <6>
    @Order(1) // <7>
    @Test
    void fetchingOctoberHeadlinesFirstTimeDoesNotUseCache() {
        News news = client.toBlocking().retrieve(createRequest(), News.class);
        assertEquals(Arrays.asList(expected), news.getHeadlines());
    }

    @Timeout(1) // <6>
    @Order(2) // <7>
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
