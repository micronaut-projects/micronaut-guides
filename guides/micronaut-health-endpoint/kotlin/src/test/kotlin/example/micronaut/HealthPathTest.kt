package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

@Property(name = "endpoints.all.path", value = "/endpoints/") // <1>
@MicronautTest
class HealthPathTest(@Client("/") val client: HttpClient) { // <2>

    @Test
    fun healthEndpointExposedAtNonDefaultEndpointsPath() {
        val status = client.toBlocking().retrieve(HttpRequest.GET<Any>("/endpoints/health"), HttpStatus::class.java) // <2>
        assertEquals(HttpStatus.OK, status)

        val e = Executable { client.toBlocking().retrieve(HttpRequest.GET<Any>("/health"), HttpStatus::class.java) } // <3>
        val thrown = assertThrows(HttpClientResponseException::class.java, e)
        assertEquals(HttpStatus.NOT_FOUND, thrown.status) // <3>
    }
}