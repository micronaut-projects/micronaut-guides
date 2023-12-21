/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.context.annotation.Primary
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.internal.verification.VerificationModeFactory.times
import java.util.Optional
import jakarta.inject.Inject

@MicronautTest
class BookControllerTest(@Client("/") val client: HttpClient) {

    @Inject
    lateinit var analyticsClient: AnalyticsClient

    @Test
    fun testMessageIsPublishedToRabbitMQWhenBookFound() {
        val result = retrieveGet("/books/1491950358")
        assertNotNull(result)
        Mockito.verify(analyticsClient, times(1)).updateAnalytics(result!!)
    }

    @Test
    fun testMessageIsNotPublishedToRabbitMQWhenBookNotFound() {
        assertThrows(HttpClientResponseException::class.java) {
            retrieveGet("/books/INVALID")
        }
        Mockito.verifyNoInteractions(analyticsClient)
    }

    @Primary
    @MockBean
    fun analyticsClient(): AnalyticsClient {
        return Mockito.mock(AnalyticsClient::class.java)
    }

    private fun retrieveGet(url: String): Book? {
        return client.toBlocking().retrieve(
            HttpRequest.GET<Any>(url),
            Argument.of(Book::class.java)
        )
    }
}
