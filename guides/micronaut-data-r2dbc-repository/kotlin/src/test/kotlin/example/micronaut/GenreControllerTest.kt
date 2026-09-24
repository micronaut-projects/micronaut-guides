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

        val genreIds = mutableListOf<Long>()

        var request = HttpRequest.POST("/genres", mapOf("name" to "DevOps")) // <3>
        var response = client.toBlocking().exchange(request, Genre::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        genreIds.add(entityId(response))

        request = HttpRequest.POST("/genres", mapOf("name" to "Microservices")) // <3>
        response = client.toBlocking().exchange(request, Genre::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        val genreId = entityId(response)
        genreIds.add(genreId)

        var genre = client.toBlocking().retrieve("/genres/$genreId", Genre::class.java) // <4>

        assertEquals("Microservices", genre.name)

        var cmdRequest = HttpRequest.PUT("/genres", GenreUpdateCommand(genreId, "Micro-services"))
        response = client.toBlocking().exchange(cmdRequest) // <5>

        assertEquals(HttpStatus.NO_CONTENT, response.status())

        genre = client.toBlocking().retrieve("/genres/$genreId", Genre::class.java)

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

        request = HttpRequest.GET("/genres/list?size=1&sort=name")
        genres= client.toBlocking().retrieve(request, Argument.listOf(Genre::class.java))

        assertEquals(1, genres.size)
        assertEquals("DevOps", genres[0].name)

        request = HttpRequest.GET("/genres/list?size=1&sort=name,desc")
        genres= client.toBlocking().retrieve(request, Argument.listOf(Genre::class.java))

        assertEquals(1, genres.size)
        assertEquals("Micro-services", genres[0].name)

        request = HttpRequest.GET("/genres/list?size=1&page=2&sort=name")
        genres= client.toBlocking().retrieve(request, Argument.listOf(Genre::class.java))

        assertEquals(0, genres.size)

        for (id in genreIds) {
            request = HttpRequest.DELETE("/genres/$id")
            response = client.toBlocking().exchange(request)

            assertEquals(HttpStatus.NO_CONTENT, response.status)
        }

        request = HttpRequest.GET("/genres/list")
        genres = client.toBlocking().retrieve(request, Argument.listOf(Genre::class.java))

        assertEquals(0, genres.size)

    }

    private fun entityId(response: HttpResponse<*>): Long {
        val location = response.header(HttpHeaders.LOCATION)
        assertNotNull(location)
        return location!!.substring("/genres/".length).toLong()
    }

}
