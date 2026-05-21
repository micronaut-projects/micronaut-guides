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

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BooksControllerTest : TestPropertyProvider {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Inject
    lateinit var idGenerator: IdGenerator

    @Inject
    lateinit var bookRepository: BookRepository

    override fun getProperties(): Map<String, String> {
        System.setProperty("aws.region", "us-east-1")
        if (!dynamoDBLocal.isRunning) {
            dynamoDBLocal.start()
        }
        return mapOf(
            "dynamodb-local.host" to "localhost",
            "dynamodb-local.port" to dynamoDBLocal.firstMappedPort.toString()
        )
    }

    @Test
    fun testRetrieveBooks() {
        val releaseIt = Book(idGenerator.generate(), "1680502395", "Release It!", null)
        bookRepository.save(releaseIt)
        val continuousDelivery = Book(idGenerator.generate(), "0321601912", "Continuous Delivery", 4)
        bookRepository.save(continuousDelivery)
        val buildingMicroservices = Book(idGenerator.generate(), "1491950358", "Building Microservices", 0)
        bookRepository.save(buildingMicroservices)

        val result = bookRepository.findById(continuousDelivery.id)
        assertTrue(result.isPresent)
        assertEquals("Continuous Delivery", result.get().name)

        val client = httpClient.toBlocking()

        var hasStock = client.retrieve(stockRequest(continuousDelivery.isbn), Boolean::class.java)
        assertTrue(hasStock)
        hasStock = client.retrieve(stockRequest(buildingMicroservices.isbn), Boolean::class.java)
        assertFalse(hasStock)

        val e = assertThrows(HttpClientResponseException::class.java) {
            client.retrieve(stockRequest(releaseIt.isbn), Boolean::class.java)
        }
        assertEquals(HttpStatus.NOT_FOUND, e.status)

        bookRepository.delete(releaseIt.id)
        bookRepository.delete(continuousDelivery.id)
        bookRepository.delete(buildingMicroservices.id)
    }

    companion object {
        @Container
        @JvmField
        val dynamoDBLocal = DynamoDbLocalContainer().withExposedPorts(8000)

        private fun stockRequest(isbn: String): HttpRequest<*> {
            val uri = UriBuilder.of("/books")
                .path("stock")
                .path(isbn)
                .build()
            return HttpRequest.GET<Any>(uri)
                .accept(MediaType.TEXT_PLAIN)
        }
    }

    class DynamoDbLocalContainer : GenericContainer<DynamoDbLocalContainer>("amazon/dynamodb-local")
}
