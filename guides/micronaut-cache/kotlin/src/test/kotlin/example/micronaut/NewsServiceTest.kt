package example.micronaut

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.Timeout
import java.time.Month
import javax.inject.Inject

@TestMethodOrder(OrderAnnotation::class) // <1>
@MicronautTest(startApplication = false)  // <2>
internal class NewsServiceTest {
    @Inject
    lateinit var newsService : NewsService // <3>

    @Timeout(4) // <4>
    @Test
    @Order(1) // <5>
    fun firstInvocationOfNovemberDoesNotHitCache() {
        val headlines = newsService.headlines(Month.NOVEMBER)
        Assertions.assertEquals(2, headlines!!.size)
    }

    @Timeout(1) // <4>
    @Test
    @Order(2) // <5>
    fun secondInvocationOfNovemberHitsCache() {
        val headlines = newsService.headlines(Month.NOVEMBER)
        Assertions.assertEquals(2, headlines!!.size)
    }

    @Timeout(4) // <4>
    @Test
    @Order(3) // <5>
    fun firstInvocationOfOctoberDoesNotHitCache() {
        val headlines = newsService.headlines(Month.OCTOBER)
        Assertions.assertEquals(1, headlines!!.size)
    }

    @Timeout(1) // <4>
    @Test
    @Order(4) // <5>
    fun secondInvocationOfOctoberHitsCache() {
        val headlines = newsService.headlines(Month.OCTOBER)
        Assertions.assertEquals(1, headlines!!.size)
    }

    @Timeout(1) // <4>
    @Test
    @Order(5) // <5>
    fun addingAHeadlineToNovemberUpdatesCache() {
        val headlines = newsService.addHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released")
        Assertions.assertEquals(3, headlines!!.size)
    }

    @Timeout(1) // <4>
    @Test
    @Order(6) // <5>
    fun novemberCacheWasUpdatedByCachePutAndThusTheValueIsRetrievedFromTheCache() {
        val headlines = newsService.headlines(Month.NOVEMBER)
        Assertions.assertEquals(3, headlines!!.size)
    }

    @Timeout(1) // <4>
    @Test
    @Order(7) // <5>
    fun invalidateNovemberCacheWithCacheInvalidate() {
        Assertions.assertDoesNotThrow { newsService.removeHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released") }
    }

    @Timeout(1) // <4>
    @Test
    @Order(8) // <5>
    fun octoberCacheIsStillValid() {
        val headlines = newsService.headlines(Month.OCTOBER)
        Assertions.assertEquals(1, headlines!!.size)
    }

    @Timeout(4) // <4>
    @Test
    @Order(9) // <5>
    fun novemberCacheWasInvalidated() {
        val headlines = newsService.headlines(Month.NOVEMBER)
        Assertions.assertEquals(2, headlines!!.size)
    }
}
