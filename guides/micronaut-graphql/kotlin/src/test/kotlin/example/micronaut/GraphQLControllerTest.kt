package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import jakarta.inject.Inject

@MicronautTest
class GraphQLControllerTest(@Client("/") val client: HttpClient) { // <2>

    @Test
    fun testGraphQLController() {
        val query = "{ \"query\": \"{ bookById(id:\\\"book-1\\\") { name, pageCount, author { firstName, lastName} } }\" }"
        val request: HttpRequest<String> = HttpRequest.POST("/graphql", query)

        val rsp = client.toBlocking().exchange(request, Argument.of(Map::class.java))
        assertEquals(HttpStatus.OK, rsp.status())
        assertNotNull(rsp.body())

        val bookInfo = rsp.getBody(Map::class.java).get()["data"] as Map<*, *>
        val bookById = bookInfo["bookById"] as Map<*, *>?
        assertEquals("Harry Potter and the Philosopher's Stone", bookById!!["name"])
        assertEquals(223, bookById["pageCount"])

        val author = bookById["author"] as Map<*, *>?
        assertEquals("Joanne", author!!["firstName"])
        assertEquals("Rowling", author["lastName"])
    }
}
