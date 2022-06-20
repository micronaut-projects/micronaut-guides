package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import jakarta.inject.Inject

@MicronautTest // <1>
class HealthTest {
    @Inject
    @field:Client("/")
    lateinit var client : HttpClient // <2>

    @Test
    fun testHealthRespondsOK() {
        val request: HttpRequest<Any> = HttpRequest.GET("/health")  // <3>
        val m = client.toBlocking().retrieve(request, Map::class.java)
        assertNotNull(m)
        assertTrue(m.containsKey("status"))
        assertEquals(m.get("status"), "UP")
    }
}