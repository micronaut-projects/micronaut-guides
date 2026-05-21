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
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
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

        val request: HttpRequest<*> = HttpRequest.GET<Any>("/books")
        val books = httpClient.toBlocking().retrieve(request, Argument.listOf(BookCatalogue::class.java))
        assertEquals(3, books.size)

        assertTrue(books.any { it.isbn == "1491950358" && it.name == "Building Microservices" })
        assertTrue(books.any { it.isbn == "0321601912" && it.name == "Continuous Delivery" })
        assertTrue(books.any { it.isbn == "1680502395" && it.name == "Release It!" })

        bookRepository.delete(releaseIt.id)
        bookRepository.delete(continuousDelivery.id)
        bookRepository.delete(buildingMicroservices.id)
    }

    companion object {
        @Container
        @JvmField
        val dynamoDBLocal = DynamoDbLocalContainer().withExposedPorts(8000)
    }

    class DynamoDbLocalContainer : GenericContainer<DynamoDbLocalContainer>("amazon/dynamodb-local")
}
