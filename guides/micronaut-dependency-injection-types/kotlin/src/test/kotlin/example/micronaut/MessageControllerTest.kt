package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@MicronautTest
class MessageControllerTest(@Client("/") val httpClient: HttpClient) {

    @ParameterizedTest
    @ValueSource(strings = ["/constructor", "/field", "/setter"])
    fun validateMessageControllers(path: String) {
        val request = HttpRequest.GET<String>(path).accept(MediaType.TEXT_PLAIN)
        assertEquals("Hello World", httpClient.toBlocking().retrieve(request))
    }
}
