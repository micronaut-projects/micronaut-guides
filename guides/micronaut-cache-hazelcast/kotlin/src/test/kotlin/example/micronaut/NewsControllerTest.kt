package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Month

@TestMethodOrder(MethodOrderer.OrderAnnotation::class) // <1>
@MicronautTest // <2>
@Testcontainers // <3>
class NewsControllerTest : TestPropertyProvider { // <4>
    @Container // <5>
    var hazelcast = GenericContainer("hazelcast/hazelcast:4.2.1")
        .withExposedPorts(5701)

    @NonNull
    override fun getProperties(): Map<String, String> { // <4>
        return mapOf("hazelcast.client.network.addresses"
                to "127.0.0.1:" + hazelcast.firstMappedPort)
    }

    @Inject
    @field:Client("/") // <6>
    lateinit var client: HttpClient

    @Timeout(12) // <7>
    @Order(1) // <8>
    @Test
    fun fetchingOctoberHeadlinesFirstTimeDoesNotUseCache() {
        val request: HttpRequest<Any> = HttpRequest.GET(UriBuilder.of("/").path(Month.OCTOBER.name).build())
        val news: News = client.toBlocking().retrieve(request, News::class.java)
        val expected = "Micronaut AOP: Awesome flexibility without the complexity"
        assertEquals(listOf(expected), news.headlines)
    }

    @Timeout(1) // <7>
    @Order(2) // <8>
    @Test
    fun fetchingOctoberHeadlinesSecondTimeUsesCache() {
        val request: HttpRequest<Any> = HttpRequest.GET(UriBuilder.of("/").path(Month.OCTOBER.name).build())
        val expected = "Micronaut AOP: Awesome flexibility without the complexity"
        val news = client.toBlocking().retrieve(request, News::class.java)
        assertEquals(listOf(expected), news.headlines)
    }
}
