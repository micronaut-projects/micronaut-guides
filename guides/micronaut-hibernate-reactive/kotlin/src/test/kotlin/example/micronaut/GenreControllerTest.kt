package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

import jakarta.inject.Inject


@MicronautTest // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
class GenreControllerTest {
    
    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient // <3>
    
    var blockingClient: BlockingHttpClient? = null

    @BeforeEach
    fun setup() {
        blockingClient = httpClient.toBlocking()
    }

    @Test
    fun supplyAnInvalidOrderTriggersValidationFailure() {
        val thrown = Assertions.assertThrows(HttpClientResponseException::class.java) {
            blockingClient!!.exchange<Any, Any>(HttpRequest.GET("/genres/list?order=foo"))
        }
        Assertions.assertNotNull(thrown.response)
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
    }

    @Test
    fun testFindNonExistingGenreReturns404() {
        val thrown = Assertions.assertThrows(HttpClientResponseException::class.java) {
            httpClient.toBlocking().exchange<Any, Any>(HttpRequest.GET("/genres/99"))
        }
        Assertions.assertNotNull(thrown.response)
        Assertions.assertEquals(HttpStatus.NOT_FOUND, thrown.status)
    }

    @Test
    fun testGenreCrudOperations() {
        val genreIds = mutableListOf<Long?>()
        var request: HttpRequest<*>? = HttpRequest.POST("/genres", GenreSaveCommand("DevOps")) // <4>
        var response: HttpResponse<Genre> = blockingClient!!.exchange(request)
        genreIds.add(entityId(response))
        Assertions.assertEquals(HttpStatus.CREATED, response.status)

        request = HttpRequest.POST("/genres", GenreSaveCommand("Microservices")) // <4>
        response = blockingClient!!.exchange(request)
        Assertions.assertEquals(HttpStatus.CREATED, response.status)

        val id = entityId(response)
        genreIds.add(id)
        request = HttpRequest.GET<Any>("/genres/$id")
        var genre = blockingClient!!.retrieve(request, Genre::class.java) // <5>
        Assertions.assertEquals("Microservices", genre.name)

        request = HttpRequest.PUT("/genres", GenreUpdateCommand(id!!, "Micro-services"))
        response = blockingClient!!.exchange(request) // <6>
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.status)

        request = HttpRequest.GET<Any>("/genres/$id")
        genre = blockingClient!!.retrieve(request, Genre::class.java)
        Assertions.assertEquals("Micro-services", genre.name)

        request = HttpRequest.GET<Any>("/genres/list")
        var genres = blockingClient!!.retrieve(
            request, Argument.listOf(Genre::class.java)
        )
        Assertions.assertEquals(2, genres.size)

        request = HttpRequest.POST("/genres/ex", GenreSaveCommand("Microservices")) // <4>
        response = blockingClient!!.exchange(request)
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.status)

        request = HttpRequest.GET<Any>("/genres/list")
        genres = blockingClient!!.retrieve(
            request, Argument.listOf(Genre::class.java)
        )
        Assertions.assertEquals(2, genres.size)

        request = HttpRequest.GET<Any>("/genres/list?max=1")
        genres = blockingClient!!.retrieve(
            request, Argument.listOf(Genre::class.java)
        )
        Assertions.assertEquals(1, genres.size)
        Assertions.assertEquals("DevOps", genres[0].name)

        request = HttpRequest.GET<Any>("/genres/list?max=1&order=desc&sort=name")
        genres = blockingClient!!.retrieve(
            request, Argument.listOf(Genre::class.java)
        )
        Assertions.assertEquals(1, genres.size)
        Assertions.assertEquals("Micro-services", genres[0].name)

        request = HttpRequest.GET<Any>("/genres/list?max=1&offset=10")
        genres = blockingClient!!.retrieve(
            request, Argument.listOf(Genre::class.java)
        )
        Assertions.assertEquals(0, genres.size)

        // cleanup:
        for (genreId in genreIds) {
            request = HttpRequest.DELETE<Any>("/genres/$genreId")
            response = blockingClient!!.exchange(request)
            Assertions.assertEquals(HttpStatus.NO_CONTENT, response.status)
        }
    }

    private fun entityId(response: HttpResponse<*>): Long? {
        val path = "/genres/"
        val value = response.header(HttpHeaders.LOCATION) ?: return null
        val id = value.substringAfter(path)
        if (id.isBlank()) return null
        return id.toLong()
    }
}

