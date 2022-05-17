package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

@MicronautTest // <1>
class ColorControllerTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient // <2>

    @Test
    fun testCompositePattern() {
        val client = httpClient.toBlocking()
        assertEquals("yellow",
            client.retrieve(HttpRequest.GET<Any>("/color").header("color", "yellow"))
        )
        assertThrows(HttpClientResponseException::class.java) {
            client.retrieve(HttpRequest.GET<Any>("/color"))
        }
        assertEquals("yellow",
            client.retrieve(HttpRequest.GET<Any>("/color/mint").header("color", "yellow"))
        )
        assertEquals("mint",
            client.retrieve(HttpRequest.GET<Any>("/color/mint"))
        )
    }
}
