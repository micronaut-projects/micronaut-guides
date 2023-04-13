package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

@MicronautTest // <1>
internal class GraphQLControllerTest(@Client("/") val client: HttpClient) { // <2>

    @Test
    fun testGraphQLController() {
        // when:
        var todos = allTodos

        // then:
        assertTrue(todos.isEmpty())

        // when:
        val id = createToDo("Test GraphQL", "Tim Yates")

        // then: (check it's a UUID)
        assertEquals(1, id)

        // when:
        todos = allTodos

        // then:
        assertEquals(1, todos.size)
        var todo = todos[0]
        assertEquals("Test GraphQL", todo["title"])
        assertFalse(java.lang.Boolean.parseBoolean(todo["completed"].toString()))
        assertEquals(
            "Tim Yates",
            (todo["author"] as Map<*, *>?)!!["username"]
        )

        // when:
        val completed = markAsCompleted(id)

        // then:
        assertTrue(completed)

        // when:
        todos = allTodos

        // then:
        assertEquals(1, todos.size)
        todo = todos[0]
        assertEquals("Test GraphQL", todo["title"])
        assertTrue(java.lang.Boolean.parseBoolean(todo["completed"].toString()))
        assertEquals(
            "Tim Yates",
            (todo["author"] as Map<*, *>?)!!["username"]
        )
    }

    private fun fetch(query: String): HttpResponse<Map<String, Any>> {
        val request: HttpRequest<String> = HttpRequest.POST("/graphql", query)
        val response = client.toBlocking().exchange(
            request, Argument.mapOf(Argument.STRING, Argument.OBJECT_ARGUMENT)
        )
        assertEquals(HttpStatus.OK, response.status())
        Assertions.assertNotNull(response.body())
        return response
    }

    private val allTodos: List<Map<String, Any>>
        get() {
            val query = "{\"query\":\"query { toDos { title, completed, author { id, username } } }\"}"
            val response = fetch(query)
            return (response.body.get()["data"] as Map<*, *>)["toDos"] as List<Map<String, Any>>
        }

    private fun createToDo(title: String, author: String): Long {
        val query =
            "{\"query\": \"mutation { createToDo(title: \\\"$title\\\", author: \\\"$author\\\") { id } }\" }"
        val response = fetch(query)
        return ((response.body.get().get("data") as Map<*, *>)["createToDo"] as Map<*, *>?)!!["id"].toString().toLong()
    }

    private fun markAsCompleted(id: Long): Boolean {
        val query = "{\"query\": \"mutation { completeToDo(id: \\\"$id\\\") }\" }"
        val response = fetch(query)
        return (response.body.get()["data"] as Map<String, Any>)["completeToDo"] as Boolean
    }
}