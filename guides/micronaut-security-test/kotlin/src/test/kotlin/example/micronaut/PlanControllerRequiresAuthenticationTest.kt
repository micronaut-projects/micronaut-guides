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
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

@MicronautTest // <1>
class PlanControllerRequiresAuthenticationTest(@Client("/") val httpClient: HttpClient) { // <2>

    @Test
    fun planControllerRequiresAuthentication() {
        val client = httpClient.toBlocking()
        val ex = assertThrows(HttpClientResponseException::class.java) {
            client.exchange<String, String>(HttpRequest.GET<String>("/plan").accept(MediaType.TEXT_PLAIN))
        }
        assertEquals(HttpStatus.UNAUTHORIZED, ex.status)
    }
}
