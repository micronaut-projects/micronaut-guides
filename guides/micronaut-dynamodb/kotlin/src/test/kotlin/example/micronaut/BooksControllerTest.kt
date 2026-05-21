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

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@Disabled
@MicronautTest // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
class BooksControllerTest { // <3>

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient // <4>

    @Test
    fun testRetrieveBooks() {
        val client = httpClient.toBlocking()

        val releaseItIsbn = "1680502395"
        val releaseItName = "Release It!"
        var saveResponse: HttpResponse<Any> = client.exchange(
            saveRequest(releaseItIsbn, releaseItName)
        )
        assertEquals(HttpStatus.CREATED, saveResponse.status())
        var location: String? = saveResponse.headers.get(HttpHeaders.LOCATION)
        assertNotNull(location)
        assertTrue(location!!.startsWith("/books/"))
        val releaseItId = location.substring("/books/".length)

        val continuousDeliveryIsbn = "0321601912"
        val continuousDeliveryName = "Continuous Delivery"
        saveResponse = client.exchange(
            saveRequest(continuousDeliveryIsbn, continuousDeliveryName)
        )
        assertEquals(HttpStatus.CREATED, saveResponse.status())
        location = saveResponse.headers.get(HttpHeaders.LOCATION)
        assertNotNull(location)
        assertTrue(location!!.startsWith("/books/"))
        val continuousDeliveryId = location.substring("/books/".length)

        val buildingMicroservicesIsbn = "1491950358"
        val buildingMicroservicesName = "Building Microservices"
        saveResponse = client.exchange(
            saveRequest(buildingMicroservicesIsbn, buildingMicroservicesName)
        )
        assertEquals(HttpStatus.CREATED, saveResponse.status())
        location = saveResponse.headers.get(HttpHeaders.LOCATION)
        assertNotNull(location)
        assertTrue(location!!.startsWith("/books/"))
        val buildingMicroservicesId = location.substring("/books/".length)

        val result = client.retrieve(
            HttpRequest.GET<Any>(
                UriBuilder.of("/books")
                    .path(continuousDeliveryId)
                    .build()
            ),
            Book::class.java
        )
        assertEquals(continuousDeliveryName, result.name)

        val books = client.retrieve(
            HttpRequest.GET<Any>("/books"),
            Argument.listOf(Book::class.java)
        )
        assertEquals(3, books.size)

        assertTrue(books.any { it.isbn == continuousDeliveryIsbn && it.name == continuousDeliveryName })
        assertTrue(books.any { it.isbn == releaseItIsbn && it.name == releaseItName })
        assertTrue(books.any { it.isbn == buildingMicroservicesIsbn && it.name == buildingMicroservicesName })

        var deleteResponse: HttpResponse<*> = client.exchange<Any, Any>(
            HttpRequest.DELETE(
                UriBuilder.of("/books")
                    .path(continuousDeliveryId)
                    .build()
                    .toString()
            )
        )
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.status)
        deleteResponse = client.exchange<Any, Any>(
            HttpRequest.DELETE(
                UriBuilder.of("/books")
                    .path(releaseItId)
                    .build()
                    .toString()
            )
        )
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.status)
        deleteResponse = client.exchange<Any, Any>(
            HttpRequest.DELETE(
                UriBuilder.of("/books")
                    .path(buildingMicroservicesId)
                    .build()
                    .toString()
            )
        )
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.status)
    }

    companion object {
        private fun saveRequest(isbn: String, name: String): HttpRequest<Map<String, String>> =
            HttpRequest.POST("/books", mapOf("isbn" to isbn, "name" to name))
    }
}
