package example.micronaut;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // <1>
@MicronautTest(startApplication = false) // <2>
class NewsServiceTest {

    @Inject // <3>
    NewsService newsService;

    @Timeout(30) // <4>
    @Order(1) // <5>
    @Test
    void firstInvocationOfNovemberDoesNotHitCache() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(2, headlines.size());
    }

    @Timeout(1) // <4>
    @Order(2) // <5>
    @Test
    void secondInvocationOfNovemberHitsCache() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(2, headlines.size());
    }

    @Timeout(4) // <4>
    @Order(3) // <5>
    @Test
    void firstInvocationOfOctoberDoesNotHitCache() {
        List<String> headlines = newsService.headlines(Month.OCTOBER);
        assertEquals(1, headlines.size());
    }

    @Timeout(1) // <4>
    @Order(4) // <5>
    @Test
    void secondInvocationOfOctoberHitsCache() {
        List<String> headlines = newsService.headlines(Month.OCTOBER);
        assertEquals(1, headlines.size());
    }

    @Timeout(1) // <4>
    @Order(5) // <5>
    @Test
    void addingAHeadlineToNovemberUpdatesCache() {
        List<String> headlines = newsService.addHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released");
        assertEquals(3, headlines.size());
    }

    @Timeout(1) // <4>
    @Order(6) // <5>
    @Test
    void novemberCacheWasUpdatedByCachePutAndThusTheValueIsRetrievedFromTheCache() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(3, headlines.size());
    }

    @Timeout(1) // <4>
    @Order(7) // <5>
    @Test
    void invalidateNovemberCacheWithCacheInvalidate() {
        assertDoesNotThrow(() -> {
            newsService.removeHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released");
        });
    }

    @Timeout(1) // <4>
    @Order(8) // <5>
    @Test
    void octoberCacheIsStillValid() {
        List<String> headlines = newsService.headlines(Month.OCTOBER);
        assertEquals(1, headlines.size());
    }

    @Timeout(4) // <4>
    @Order(9) // <5>
    @Test
    void novemberCacheWasInvalidated() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(2, headlines.size());
    }

}
