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

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

@MicronautTest // <1>
class ColorControllerTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient // <2>

    @Test
    fun testCompositePattern() {
        val client = httpClient.toBlocking()
        assertEquals("yellow",
            client.retrieve(HttpRequest.GET<Any>("/color").header("color", "yellow"))
        )
        assertThrows(HttpClientResponseException::class.java) {
            client.retrieve(HttpRequest.GET<Any>("/color"))
        }
        assertEquals("yellow",
            client.retrieve(HttpRequest.GET<Any>("/color/mint").header("color", "yellow"))
        )
        assertEquals("mint",
            client.retrieve(HttpRequest.GET<Any>("/color/mint"))
        )
    }
}
