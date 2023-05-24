package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@MicronautTest(transactional = false) //<1>
class GenreControllerTest(@Client("/") val client: HttpClient) { // <2>

    @Test
    fun testFindNonExistingGenreReturn404() {
        val thrown = assertThrows<HttpClientResponseException>{
            client.toBlocking().exchange<Any>("/genres/99")
        }

        assertNotNull(thrown.response)
        assertEquals(HttpStatus.NOT_FOUND, thrown.status)
    }

    @Test
    fun testGenreCrudOperations() {

        var request = HttpRequest.POST("/genres", mapOf("name" to "DevOps")) // <3>
        var response = client.toBlocking().exchange(request, Genre::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertEquals("/genres/1", response.header(HttpHeaders.LOCATION))

        request = HttpRequest.POST("/genres", mapOf("name" to "Microservices")) // <3>
        response = client.toBlocking().exchange(request, Genre::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertEquals("/genres/2", response.header(HttpHeaders.LOCATION))

        var genre = client.toBlocking().retrieve("/genres/2", Genre::class.java) // <4>

        assertEquals("Microservices", genre.name)

        var cmdRequest = HttpRequest.PUT("/genres", GenreUpdateCommand(2, "Micro-services"))
        response = client.toBlocking().exchange(cmdRequest) // <5>

        assertEquals(HttpStatus.NO_CONTENT, response.status())

        genre = client.toBlocking().retrieve("/genres/2", Genre::class.java)

        assertEquals("Micro-services", genre.name)

        request = HttpRequest.GET("/genres/list")
        var genres = client.toBlocking().retrieve(request, Argument.listOf(Genre::class.java))

        assertEquals(2, genres.size)

        request = HttpRequest.POST("/genres/ex", mapOf("name" to "Microservices"))
        response = client.toBlocking().exchange(request)

        assertEquals(HttpStatus.NO_CONTENT, response.status)

        request = HttpRequest.GET("/genres/list")
        genres = client.toBlocking().retrieve(request, Argument.listOf(Genre::class.java))

        assertEquals(2, genres.size)

        request = HttpRequest.GET("/genres/list?size=1")
        genres= client.toBlocking().retrieve(request, Argument.listOf(Genre::class.java))

        assertEquals(1, genres.size)
        assertEquals("DevOps", genres[0].name)

        request = HttpRequest.GET("/genres/list?size=1&sort=name,desc")
        genres= client.toBlocking().retrieve(request, Argument.listOf(Genre::class.java))

        assertEquals(1, genres.size)
        assertEquals("Micro-services", genres[0].name)

        request = HttpRequest.GET("/genres/list?size=1&page=2")
        genres= client.toBlocking().retrieve(request, Argument.listOf(Genre::class.java))

        assertEquals(0, genres.size)

        for (i in 1..2) {
            request = HttpRequest.DELETE("/genres/$i")
            response = client.toBlocking().exchange(request)

            assertEquals(HttpStatus.NO_CONTENT, response.status)
        }

        request = HttpRequest.GET("/genres/list")
        genres = client.toBlocking().retrieve(request, Argument.listOf(Genre::class.java))

        assertEquals(0, genres.size)

    }

}