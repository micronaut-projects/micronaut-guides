package example.micronaut;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;

import javax.inject.Inject;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // <1>
@MicronautTest // <2>
class NewsServiceTest {

    @Inject // <3>
    NewsService newsService;

    @Timeout(4) // <4>
    @Test
    @Order(1) // <5>
    public void firstInvocationOfNovemberDoesNotHitCache() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(2, headlines.size());
    }

    @Timeout(1) // <4>
    @Test
    @Order(2) // <5>
    public void secondInvocationOfNovemberHitsCache() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(2, headlines.size());
    }

    @Timeout(4) // <4>
    @Test
    @Order(3) // <5>
    public void firstInvocationOfOctoberDoesNotHitCache() {
        List<String> headlines = newsService.headlines(Month.OCTOBER);
        assertEquals(1, headlines.size());
    }

    @Timeout(1) // <4>
    @Test
    @Order(4) // <5>
    public void secondInvocationOfOctoberHitsCache() {
        List<String> headlines = newsService.headlines(Month.OCTOBER);
        assertEquals(1, headlines.size());
    }

    @Timeout(1) // <4>
    @Test
    @Order(5) // <5>
    public void addingAHeadlineToNovemberUpdatesCache() {
        List<String> headlines = newsService.addHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released");
        assertEquals(3, headlines.size());
    }

    @Timeout(1) // <4>
    @Test
    @Order(6) // <5>
    public void novemberCacheWasUpdatedByCachePutAndThusTheValueIsRetrievedFromTheCache() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(3, headlines.size());
    }

    @Timeout(1) // <4>
    @Test
    @Order(7) // <5>
    public void invalidateNovemberCacheWithCacheInvalidate() {
        assertDoesNotThrow(() -> {
            newsService.removeHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released");
        });
    }

    @Timeout(1) // <4>
    @Test
    @Order(8) // <5>
    public void octoberCacheIsStillValid() {
        List<String> headlines = newsService.headlines(Month.OCTOBER);
        assertEquals(1, headlines.size());
    }

    @Timeout(4) // <4>
    @Test
    @Order(9) // <5>
    public void novemberCacheWasInvalidated() {
        List<String> headlines = newsService.headlines(Month.NOVEMBER);
        assertEquals(2, headlines.size());
    }
}
