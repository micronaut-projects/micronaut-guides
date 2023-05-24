package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

@Property(name = "endpoints.health.disk-space.threshold", value = "999999999999999999") // <1>
@MicronautTest
class PoorHealthTest(@Client("/") val client: HttpClient) { // <2>

    @Test
    fun healthEndpointExposed() {
        val e = Executable { client.toBlocking().retrieve(HttpRequest.GET<Any>("/health")) }
        val thrown = assertThrows(HttpClientResponseException::class.java, e)
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, thrown.status) // <2>
        assertTrue(thrown.response.getBody(String::class.java).orElse("").contains("DOWN")) // <3>
    }
}