package example.micronaut

import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@MicronautTest // <1>
class HelloControllerTest(@Client("/") val httpClient: HttpClient) {

    @Test
    fun testHello() {
        val body = httpClient.toBlocking().retrieve("/")
        assertNotNull(body)
        assertEquals("{\"message\":\"Hello World\"}", body)
    }
}