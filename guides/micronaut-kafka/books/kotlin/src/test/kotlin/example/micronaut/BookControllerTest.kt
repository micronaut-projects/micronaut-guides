/*
 * Copyright 2017-2024 original authors
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

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import java.util.Optional
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.TimeUnit.SECONDS
import jakarta.inject.Inject

@MicronautTest
@TestInstance(PER_CLASS) // <1>
class BookControllerTest {

    companion object {
        val received: MutableCollection<Book> = ConcurrentLinkedDeque()
    }

    @Inject
    lateinit var analyticsListener: AnalyticsListener // <2>

    @Inject // <3>
    @field:Client("/") // <4>
    lateinit var client: HttpClient // <5>

    @Test
    fun testMessageIsPublishedToKafkaWhenBookFound() {
        val isbn = "1491950358"

        val result : Optional<Book> = retrieveGet("/books/" + isbn) as Optional<Book> // <6>
        assertNotNull(result)
        assertTrue(result.isPresent)
        assertEquals(isbn, result.get().isbn)

        await().atMost(5, SECONDS).until { !received.isEmpty() } // <7>
        assertEquals(1, received.size) // <8>

        val bookFromKafka = received.iterator().next()
        assertNotNull(bookFromKafka)
        assertEquals(isbn, bookFromKafka.isbn)
    }

    @Test
    fun testMessageIsNotPublishedToKafkaWhenBookNotFound() {
        assertThrows(HttpClientResponseException::class.java) { retrieveGet("/books/INVALID") }

        Thread.sleep(5_000); // <9>
        assertEquals(0, received.size);
    }

    @AfterEach
    fun cleanup() {
        received.clear()
    }

    private fun retrieveGet(url: String) = client
            .toBlocking()
            .retrieve(HttpRequest.GET<Any>(url),
                    Argument.of(Optional::class.java, Book::class.java))

    @KafkaListener(offsetReset = EARLIEST)
    class AnalyticsListener {

        @Topic("analytics")
        fun updateAnalytics(book: Book) {
            received.add(book)
        }
    }
}
