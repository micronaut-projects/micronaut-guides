package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import jakarta.inject.Inject;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // <1>
@MicronautTest(startApplication = false) // <2>
@TestInstance(PER_CLASS) // <3>
class NewsServiceTest implements TestPropertyProvider { // <4>

    @Override
    @NonNull
    public Map<String, String> getProperties() { // <4>
        return Collections.singletonMap("hazelcast.client.network.addresses",
                HazelcastUtils.getUrl());
    }

    @AfterAll
    static void cleanup() {
        HazelcastUtils.close();
    }

    @Inject // <5>
    NewsService newsService;

    @Timeout(30) // <6>
    @Order(1) // <7>
    @Test
    public void firstInvocationOfNovemberDoesNotHitCache() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(2, headlines.size());
    }

    @Timeout(1) // <6>
    @Order(2) // <7>
    @Test
    public void secondInvocationOfNovemberHitsCache() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(2, headlines.size());
    }

    @Timeout(4) // <6>
    @Order(3) // <7>
    @Test
    public void firstInvocationOfOctoberDoesNotHitCache() {
        List<String> headlines = newsService.headlines(Month.OCTOBER);
        assertEquals(1, headlines.size());
    }

    @Timeout(1) // <6>
    @Order(4) // <7>
    @Test
    public void secondInvocationOfOctoberHitsCache() {
        List<String> headlines = newsService.headlines(Month.OCTOBER);
        assertEquals(1, headlines.size());
    }

    @Timeout(1) // <6>
    @Order(5) // <7>
    @Test
    public void addingAHeadlineToNovemberUpdatesCache() {
        List<String> headlines = newsService.addHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released");
        assertEquals(3, headlines.size());
    }

    @Timeout(1) // <6>
    @Order(6) // <7>
    @Test
    public void novemberCacheWasUpdatedByCachePutAndThusTheValueIsRetrievedFromTheCache() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(3, headlines.size());
    }

    @Timeout(1) // <6>
    @Order(7) // <7>
    @Test
    public void invalidateNovemberCacheWithCacheInvalidate() {
        assertDoesNotThrow(() -> {
            newsService.removeHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released");
        });
    }

    @Timeout(1) // <6>
    @Order(8) // <7>
    @Test
    public void octoberCacheIsStillValid() {
        List<String> headlines = newsService.headlines(Month.OCTOBER);
        assertEquals(1, headlines.size());
    }

    @Timeout(4) // <6>
    @Order(9) // <7>
    @Test
    public void novemberCacheWasInvalidated() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(2, headlines.size());
    }

}
