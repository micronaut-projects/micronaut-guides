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

import io.micronaut.context.annotation.Property
import io.micronaut.core.util.StringUtils
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE) // <1>
@MicronautTest // <2>
class LocaleControllerTest {

    @Test
    fun localeIsSavedInACookie(@Client("/") httpClient: HttpClient) { // <3>
        val client = httpClient.toBlocking()
        val request = HttpRequest.POST("/locale", mapOf("locale" to "es"))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        val response = assertDoesNotThrow { client.exchange<Map<String, String>, Any>(request) } // <4>
        assertEquals(HttpStatus.SEE_OTHER, response.status())
        val cookie = response.getCookie("locale") // <5>
        assertTrue(cookie.isPresent)
        assertEquals("es", cookie.get().value)
    }
}
