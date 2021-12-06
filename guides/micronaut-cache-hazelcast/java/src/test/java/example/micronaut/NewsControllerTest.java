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
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

import jakarta.inject.Inject;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // <1>
@MicronautTest // <2>
@Testcontainers // <3>
public class NewsControllerTest implements TestPropertyProvider { // <4>

    @Container // <5>
    static GenericContainer hazelcast = new GenericContainer("hazelcast/hazelcast:4.2.1")
            .withExposedPorts(5701);

    @Override
    @NonNull
    public Map<String, String> getProperties() { // <4>
        return Collections.singletonMap("hazelcast.client.network.addresses",
                "127.0.0.1:" + hazelcast.getFirstMappedPort());
    }

    @Inject
    @Client("/") // <6>
    HttpClient client;

    static String expected = "Micronaut AOP: Awesome flexibility without the complexity";

    @Timeout(12) // <7>
    @Order(1) // <8>
    @Test
    void fetchingOctoberHeadlinesFirstTimeDoesNotUseCache() {
        News news = client.toBlocking().retrieve(createRequest(), News.class);
        assertEquals(Arrays.asList(expected), news.getHeadlines());
    }

    @Timeout(1) // <7>
    @Order(2) // <8>
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
