package example.micronaut

import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@MicronautTest // <1>
class HelloWorldControllerTest {

    @Inject
    @field:Client("/") // <2>
    lateinit var httpClient: HttpClient

    @Test
    fun useOfLocalizedMessageSource() {
        val client = httpClient.toBlocking()
        var request: HttpRequest<*>? = HttpRequest.GET<Any>("/")
            .header(HttpHeaders.ACCEPT_LANGUAGE, "es") // <3>
        assertEquals("Hola Mundo", client.retrieve(request))
        request = HttpRequest.GET<Any>("/")
        assertEquals("Hello World", client.retrieve(request))
    }
}
