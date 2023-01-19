package example.micronaut

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import jakarta.inject.Inject

@MicronautTest
class StaticResourceTest(@Client("/") val client: HttpClient) {

    @Test
    fun staticResourcesAreExposedAtPublic() {
        val response: HttpResponse<*> = client.toBlocking().exchange<Any>("/index.html")
        assertEquals(HttpStatus.OK, response.status())
    }
}