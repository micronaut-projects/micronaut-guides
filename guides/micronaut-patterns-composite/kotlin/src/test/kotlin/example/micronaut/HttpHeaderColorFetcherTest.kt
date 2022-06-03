package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.Optional

@Property(name = "spec.name", value = "HttpHeaderColorFetcherTest") // <1>
@MicronautTest
class HttpHeaderColorFetcherTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient // <2>

    @Test
    fun theHttpHeaderColorFetcherFetchesFromColorHeader() {
        val client = httpClient.toBlocking()
        assertEquals(
            "yellow",
            client.retrieve(HttpRequest.GET<Any>("/header/color").header("color", "yellow"))
        )
        assertThrows(HttpClientResponseException::class.java) {
            client.retrieve(HttpRequest.GET<Any>("/header/color"))
        }
    }

    @Requires(property = "spec.name", value = "HttpHeaderColorFetcherTest") // <1>
    @Controller("/header")
    class HttpHeaderColorFetcherTestController(private val colorFetcher: HttpHeaderColorFetcher) {

        @Produces(MediaType.TEXT_PLAIN)
        @Get("/color")
        fun index(request: HttpRequest<*>): Optional<String> =
            colorFetcher.favouriteColor(request)
    }
}
