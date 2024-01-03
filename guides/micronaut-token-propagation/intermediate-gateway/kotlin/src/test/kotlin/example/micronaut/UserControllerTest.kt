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
import io.micronaut.http.HttpStatus.UNAUTHORIZED
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

@MicronautTest // <1>
class UserControllerTest(@Client("/") val client: HttpClient) { // <2>

    @Test
    fun testUserEndpointIsSecured() { // <3>
        val thrown = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange<Any, Any>(HttpRequest.GET("/user"))
        }
        assertEquals(UNAUTHORIZED, thrown.response.status)
    }

    @Test
    fun testAuthenticatedCanFetchUsername() {
        val credentials = UsernamePasswordCredentials("sherlock", "password")
        val request: HttpRequest<*> = HttpRequest.POST("/login", credentials)
        val bearerAccessRefreshToken = client.toBlocking().retrieve(request, BearerAccessRefreshToken::class.java)
        val username = client.toBlocking()
                .retrieve(HttpRequest.GET<Any>("/user")
                        .header("Authorization", "Bearer " + bearerAccessRefreshToken.accessToken), String::class.java)
        assertEquals("sherlock", username)
    }
}
