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

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@MicronautTest // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
class FruitDuplicationExceptionHandlerTest : BaseTest() {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient // <3>

    @Test
    fun duplicatedFruitsReturns400() {
        val banana = FruitCommand("Banana")
        val request = HttpRequest.POST("/fruits", banana)
        val response = httpClient.toBlocking().exchange<FruitCommand, Any>(request)
        assertEquals(HttpStatus.CREATED, response.status())
        val exception = assertThrows(HttpClientResponseException::class.java) {
            httpClient.toBlocking().exchange<FruitCommand, Any>(request)
        }
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.status)

        val deleteRequest = HttpRequest.DELETE("/fruits", banana)
        val deleteResponse = httpClient.toBlocking().exchange<FruitCommand, Any>(deleteRequest)
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.status())
    }
}