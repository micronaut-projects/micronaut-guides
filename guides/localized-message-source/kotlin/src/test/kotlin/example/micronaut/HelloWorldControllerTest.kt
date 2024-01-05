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

import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@MicronautTest // <1>
class HelloWorldControllerTest {

    @Inject
    @field:Client("/") // <2>
    lateinit var httpClient: HttpClient

    @Test
    fun useOfLocalizedMessageSource() {
        val client = httpClient.toBlocking()
        var request: HttpRequest<*>? = HttpRequest.GET<Any>("/")
            .header(HttpHeaders.ACCEPT_LANGUAGE, "es") // <3>
        assertEquals("Hola Mundo", client.retrieve(request))
        request = HttpRequest.GET<Any>("/")
        assertEquals("Hello World", client.retrieve(request))
    }
}
