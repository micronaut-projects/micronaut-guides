package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.time.Month
import javax.inject.Inject

@MicronautTest
class NewsControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Timeout(5) // <1>
    @Test
    fun fetchingOctoberHeadlinesUsesCache() {
        val request: HttpRequest<Any> = HttpRequest.GET(UriBuilder.of("/").path(Month.OCTOBER.name).build())
        var news: News = client.toBlocking().retrieve(request, News::class.java)
        val expected = "Micronaut AOP: Awesome flexibility without the complexity"
        Assertions.assertEquals(listOf(expected), news.headlines)
        news = client.toBlocking().retrieve(request, News::class.java)
        Assertions.assertEquals(listOf(expected), news.headlines)
    }
}
