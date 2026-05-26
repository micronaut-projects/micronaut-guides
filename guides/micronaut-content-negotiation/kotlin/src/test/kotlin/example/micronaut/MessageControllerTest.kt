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
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Test

@MicronautTest // <1>
class MessageControllerTest(@Client("/") val httpClient: HttpClient) { // <2>

    @Test
    fun contentNegotiation() {
        val client = httpClient.toBlocking()

        val expectedJson = """{"message":"Hello World"}"""
        val json = assertDoesNotThrow<String> {
            client.retrieve(HttpRequest.GET<Any>("/")
                .accept(MediaType.APPLICATION_JSON)) // <3>
        }
        assertEquals(expectedJson, json)

        val expectedHtml = """
            <!DOCTYPE html>
            <html lang="en">
            <body>
            <h1>Hello World</h1>
            </body>
            </html>
        """.trimIndent() + "\n"
        val html = assertDoesNotThrow<String> {
            client.retrieve(HttpRequest.GET<Any>("/")
                .accept(MediaType.TEXT_HTML))
        }
        assertEquals(expectedHtml, html)
    }
}
