/*
 * Copyright 2017-2026 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@MicronautTest // <1>
class GenreControllerTest {

    private lateinit var blockingClient: BlockingHttpClient

    @Inject
    @field:Client("/")
    lateinit var client: HttpClient // <2>

    @BeforeEach
    fun setup() {
        blockingClient = client.toBlocking()
    }

    @Test
    fun supplyAnInvalidOrderTriggersValidationFailure() {
        val thrown = assertThrows(HttpClientResponseException::class.java) {
            blockingClient.exchange<Any, Any>(HttpRequest.GET("/genres/list?order=foo"))
        }

        assertNotNull(thrown.response)
        assertEquals(HttpStatus.BAD_REQUEST, thrown.status)
    }

    @Test
    fun testFindNonExistingGenreReturns404() {
        val thrown = assertThrows(HttpClientResponseException::class.java) {
            blockingClient.exchange<Any, Any>(HttpRequest.GET("/genres/99"))
        }

        assertNotNull(thrown.response)
        assertEquals(HttpStatus.NOT_FOUND, thrown.status)
    }

    @Test
    fun testGenreCrudOperations() {
        val genreIds = mutableListOf<Long?>()

        val devOpsRequest = HttpRequest.POST("/genres", GenreSaveCommand("DevOps")) // <3>
        var response: HttpResponse<Genre> = blockingClient.exchange(devOpsRequest, Genre::class.java)
        genreIds.add(entityId(response))

        assertEquals(HttpStatus.CREATED, response.status)

        val microservicesRequest = HttpRequest.POST("/genres", GenreSaveCommand("Microservices")) // <3>
        response = blockingClient.exchange(microservicesRequest, Genre::class.java)

        assertEquals(HttpStatus.CREATED, response.status)

        val id = entityId(response)
        genreIds.add(id)
        var getRequest = HttpRequest.GET<Any>("/genres/$id")

        var genre = blockingClient.retrieve(getRequest, Genre::class.java) // <4>

        assertEquals("Microservices", genre.name)

        val updateRequest = HttpRequest.PUT("/genres", GenreUpdateCommand(id!!, "Micro-services"))
        response = blockingClient.exchange(updateRequest, Genre::class.java) // <5>

        assertEquals(HttpStatus.NO_CONTENT, response.status)

        getRequest = HttpRequest.GET("/genres/$id")
        genre = blockingClient.retrieve(getRequest, Genre::class.java)
        assertEquals("Micro-services", genre.name)

        getRequest = HttpRequest.GET("/genres/list")
        var genres = blockingClient.retrieve(getRequest, Argument.listOf(Genre::class.java))

        assertEquals(2, genres.size)

        val exceptionRequest = HttpRequest.POST("/genres/ex", GenreSaveCommand("Microservices")) // <3>
        response = blockingClient.exchange(exceptionRequest, Genre::class.java)

        assertEquals(HttpStatus.NO_CONTENT, response.status)

        getRequest = HttpRequest.GET("/genres/list")
        genres = blockingClient.retrieve(getRequest, Argument.listOf(Genre::class.java))

        assertEquals(2, genres.size)

        getRequest = HttpRequest.GET("/genres/list?max=1")
        genres = blockingClient.retrieve(getRequest, Argument.listOf(Genre::class.java))

        assertEquals(1, genres.size)
        assertEquals("DevOps", genres[0].name)

        getRequest = HttpRequest.GET("/genres/list?max=1&order=desc&sort=name")
        genres = blockingClient.retrieve(getRequest, Argument.listOf(Genre::class.java))

        assertEquals(1, genres.size)
        assertEquals("Micro-services", genres[0].name)

        getRequest = HttpRequest.GET("/genres/list?max=1&offset=10")
        genres = blockingClient.retrieve(getRequest, Argument.listOf(Genre::class.java))

        assertEquals(0, genres.size)

        // cleanup:
        for (genreId in genreIds) {
            val deleteRequest = HttpRequest.DELETE<Any>("/genres/$genreId")
            response = blockingClient.exchange(deleteRequest, Genre::class.java)
            assertEquals(HttpStatus.NO_CONTENT, response.status)
        }
    }

    private fun entityId(response: HttpResponse<*>): Long? {
        val path = "/genres/"
        val value = response.header(HttpHeaders.LOCATION) ?: return null
        val id = value.substringAfter(path)
        if (id.isBlank()) {
            return null
        }
        return id.toLong()
    }
}
