package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <1>
@MicronautTest // <2>
class BookControllerTest : AbstractTest() { // <3>

    @Test
    fun bookUpsert(repository: BookRepository, // <4>
                   @Client("/") httpClient: HttpClient) { // <5>
        val client = httpClient.toBlocking()
        val path = "/books"
        val title = "Netty in Action"
        assertNull(repository.findByTitle(title)) // <6>

        val postResponse = client.exchange<Map<String, Any>, Any>(
            HttpRequest.POST(path, mapOf("id" to 123L, "title" to title)))
        assertEquals(HttpStatus.CREATED, postResponse.status) // <7>
        assertNotNull(repository.findByTitle(title))
        assertNull(repository.findByTitle(title)?.pages)

        val putResponse = client.exchange<Map<String, Any>, Any>(
            HttpRequest.PUT(path, mapOf("id" to 123L, "title" to title, "pages" to 265)))
        assertEquals(HttpStatus.OK, putResponse.status) // <8>
        assertNotNull(repository.findByTitle(title)?.pages) // <9>
    }
}
