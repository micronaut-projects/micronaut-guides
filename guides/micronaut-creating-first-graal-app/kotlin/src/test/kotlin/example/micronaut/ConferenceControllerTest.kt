package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import jakarta.inject.Inject

@MicronautTest
class ConferenceControllerTest {

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient

    @Test
    fun testHello() {
        val request: HttpRequest<*> = HttpRequest.GET<Any>("/conferences/random")
        val body = client.toBlocking().retrieve(request)
        assertNotNull(body)
    }
}
