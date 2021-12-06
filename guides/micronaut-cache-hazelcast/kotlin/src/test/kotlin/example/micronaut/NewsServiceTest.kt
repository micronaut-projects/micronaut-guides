package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.Timeout
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Month

@TestMethodOrder(OrderAnnotation::class) // <1>
@MicronautTest(startApplication = false)  // <2>
@Testcontainers // <3>
internal class NewsServiceTest : TestPropertyProvider { // <4>
    @Container // <5>
    var hazelcast = GenericContainer("hazelcast/hazelcast:4.2.1")
        .withExposedPorts(5701)

    @NonNull
    override fun getProperties(): Map<String, String> { // <4>
        return mapOf("hazelcast.client.network.addresses"
                to "127.0.0.1:" + hazelcast.firstMappedPort)
    }

    @Inject
    lateinit var newsService : NewsService // <6>

    @Timeout(12) // <7>
    @Test
    @Order(1) // <8>
    fun firstInvocationOfNovemberDoesNotHitCache() {
        val headlines = newsService.headlines(Month.NOVEMBER)
        assertEquals(2, headlines!!.size)
    }

    @Timeout(1) // <7>
    @Test
    @Order(2) // <8>
    fun secondInvocationOfNovemberHitsCache() {
        val headlines = newsService.headlines(Month.NOVEMBER)
        assertEquals(2, headlines!!.size)
    }

    @Timeout(4) // <7>
    @Test
    @Order(3) // <8>
    fun firstInvocationOfOctoberDoesNotHitCache() {
        val headlines = newsService.headlines(Month.OCTOBER)
        assertEquals(1, headlines!!.size)
    }

    @Timeout(1) // <7>
    @Test
    @Order(4) // <8>
    fun secondInvocationOfOctoberHitsCache() {
        val headlines = newsService.headlines(Month.OCTOBER)
        assertEquals(1, headlines!!.size)
    }

    @Timeout(1) // <7>
    @Test
    @Order(5) // <8>
    fun addingAHeadlineToNovemberUpdatesCache() {
        val headlines = newsService.addHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released")
        assertEquals(3, headlines!!.size)
    }

    @Timeout(1) // <7>
    @Test
    @Order(6) // <8>
    fun novemberCacheWasUpdatedByCachePutAndThusTheValueIsRetrievedFromTheCache() {
        val headlines = newsService.headlines(Month.NOVEMBER)
        assertEquals(3, headlines!!.size)
    }

    @Timeout(1) // <7>
    @Test
    @Order(7) // <8>
    fun invalidateNovemberCacheWithCacheInvalidate() {
        assertDoesNotThrow { newsService.removeHeadline(Month.NOVEMBER, "Micronaut 1.3 Milestone 1 Released") }
    }

    @Timeout(1) // <7>
    @Test
    @Order(8) // <8>
    fun octoberCacheIsStillValid() {
        val headlines = newsService.headlines(Month.OCTOBER)
        assertEquals(1, headlines!!.size)
    }

    @Timeout(4) // <7>
    @Test
    @Order(9) // <8>
    fun novemberCacheWasInvalidated() {
        val headlines = newsService.headlines(Month.NOVEMBER)
        assertEquals(2, headlines!!.size)
    }
}
