package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Month

@TestMethodOrder(OrderAnnotation::class) // <1>
@MicronautTest(startApplication = false)  // <2>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <3>
internal class NewsServiceTest : TestPropertyProvider { // <4>

    @NonNull
    override fun getProperties(): Map<String, String> { // <4>
        return mapOf("hazelcast.client.network.addresses"
                to HazelcastUtils.url())
    }

    @AfterAll
    fun cleanup() {
        HazelcastUtils.close()
    }

    @Inject
    lateinit var newsService : NewsService // <5>

    @Timeout(30) // <6>
    @Test
    @Order(1) // <7>
    fun firstInvocationOfNovemberDoesNotHitCache() {
        val headlines = newsService.headlines(Month.NOVEMBER)
        assertEquals(2, headlines!!.size)
    }

    @Timeout(1) // <6>
    @Test
    @Order(2) // <7>
    fun secondInvocationOfNovemberHitsCache() {
        val headlines = newsService.headlines(Month.NOVEMBER)
        assertEquals(2, headlines!!.size)
    }

    @Timeout(4) // <6>
    @Test
    @Order(3) // <7>
    fun firstInvocationOfOctoberDoesNotHitCache() {
        val headlines = newsService.headlines(Month.OCTOBER)
        assertEquals(1, headlines!!.size)
    }

    @Timeout(1) // <6>
    @Test
    @Order(4) // <7>
    fun secondInvocationOfOctoberHitsCache() {
        val headlines = newsService.headlines(Month.OCTOBER)
        assertEquals(1, headlines!!.size)
    }

    @Timeout(1) // <6>
    @Test
    @Order(5) // <7>
    fun addingAHeadlineToNovemberUpdatesCache() {
        val headlines = newsService.addHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released")
        assertEquals(3, headlines!!.size)
    }

    @Timeout(1) // <6>
    @Test
    @Order(6) // <7>
    fun novemberCacheWasUpdatedByCachePutAndThusTheValueIsRetrievedFromTheCache() {
        val headlines = newsService.headlines(Month.NOVEMBER)
        assertEquals(3, headlines!!.size)
    }

    @Timeout(1) // <6>
    @Test
    @Order(7) // <7>
    fun invalidateNovemberCacheWithCacheInvalidate() {
        assertDoesNotThrow { newsService.removeHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released") }
    }

    @Timeout(1) // <6>
    @Test
    @Order(8) // <7>
    fun octoberCacheIsStillValid() {
        val headlines = newsService.headlines(Month.OCTOBER)
        assertEquals(1, headlines!!.size)
    }

    @Timeout(4) // <6>
    @Test
    @Order(9) // <7>
    fun novemberCacheWasInvalidated() {
        val headlines = newsService.headlines(Month.NOVEMBER)
        assertEquals(2, headlines!!.size)
    }
}
