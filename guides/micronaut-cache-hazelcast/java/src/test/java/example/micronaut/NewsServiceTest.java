package example.micronaut;

import io.micronaut.core.annotation.NonNull;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // <1>
@MicronautTest(startApplication = false) // <2>
@Testcontainers // <3>
class NewsServiceTest implements TestPropertyProvider { // <4>

    @Container // <5>
    static GenericContainer hazelcast = new GenericContainer("hazelcast/hazelcast:4.2.1")
            .withExposedPorts(5701);

    @Override
    @NonNull
    public Map<String, String> getProperties() { // <4>
        return Collections.singletonMap("hazelcast.client.network.addresses",
                "127.0.0.1:" + hazelcast.getFirstMappedPort());
    }

    @Inject // <6>
    NewsService newsService;

    @Timeout(12) // <7>
    @Order(1) // <8>
    @Test
    public void firstInvocationOfNovemberDoesNotHitCache() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(2, headlines.size());
    }

    @Timeout(1) // <7>
    @Order(2) // <8>
    @Test    
    public void secondInvocationOfNovemberHitsCache() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(2, headlines.size());
    }

    @Timeout(4) // <7>
    @Order(3) // <8>
    @Test
    public void firstInvocationOfOctoberDoesNotHitCache() {
        List<String> headlines = newsService.headlines(Month.OCTOBER);
        assertEquals(1, headlines.size());
    }

    @Timeout(1) // <7>
    @Order(4) // <8>
    @Test
    public void secondInvocationOfOctoberHitsCache() {
        List<String> headlines = newsService.headlines(Month.OCTOBER);
        assertEquals(1, headlines.size());
    }

    @Timeout(1) // <7>
    @Order(5) // <8>
    @Test
    public void addingAHeadlineToNovemberUpdatesCache() {
        List<String> headlines = newsService.addHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released");
        assertEquals(3, headlines.size());
    }

    @Timeout(1) // <7>
    @Order(6) // <8>
    @Test
    public void novemberCacheWasUpdatedByCachePutAndThusTheValueIsRetrievedFromTheCache() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(3, headlines.size());
    }

    @Timeout(1) // <7>
    @Order(7) // <8>
    @Test        
    public void invalidateNovemberCacheWithCacheInvalidate() {
        assertDoesNotThrow(() -> {
            newsService.removeHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released");
        });
    }

    @Timeout(1) // <7>
    @Order(8) // <8>
    @Test        
    public void octoberCacheIsStillValid() {
        List<String> headlines = newsService.headlines(Month.OCTOBER);
        assertEquals(1, headlines.size());
    }

    @Timeout(4) // <7>
    @Order(9) // <8>
    @Test
    public void novemberCacheWasInvalidated() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(2, headlines.size());
    }
}
