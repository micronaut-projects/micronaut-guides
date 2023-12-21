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

import io.micronaut.context.annotation.Property
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
import org.junit.jupiter.api.function.Executable

@Property(name = "endpoints.all.path", value = "/endpoints/") // <1>
@MicronautTest
class HealthPathTest(@Client("/") val client: HttpClient) { // <2>

    @Test
    fun healthEndpointExposedAtNonDefaultEndpointsPath() {
        val status = client.toBlocking().retrieve(HttpRequest.GET<Any>("/endpoints/health"), HttpStatus::class.java) // <2>
        assertEquals(HttpStatus.OK, status)

        val e = Executable { client.toBlocking().retrieve(HttpRequest.GET<Any>("/health"), HttpStatus::class.java) } // <3>
        val thrown = assertThrows(HttpClientResponseException::class.java, e)
        assertEquals(HttpStatus.NOT_FOUND, thrown.status) // <3>
    }
}