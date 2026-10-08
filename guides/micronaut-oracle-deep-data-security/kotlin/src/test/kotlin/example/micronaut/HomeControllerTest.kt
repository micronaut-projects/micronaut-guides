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

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.security.authentication.AuthenticationFailureReason
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@Sql("classpath:employees-test-data.sql")
@MicronautTest // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HomeControllerTest : TestPropertyProvider {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    override fun getProperties(): Map<String, String> =
        Oracle.getProperties() + mapOf("spec.name" to "HomeControllerTest", "micronaut.server.port" to "-1") // <2>

    @Test
    fun anonymousUsersAreInvitedToSignIn() {
        val client = httpClient.toBlocking()
        val html = client.retrieve(HttpRequest.GET<Any>("/").accept(MediaType.TEXT_HTML))

        assertTrue(html.contains("Anonymous"))
        assertTrue(html.contains("Sign in with Microsoft Entra ID"))
        assertTrue(html.contains("href=\"/oauth/login/entraid\"")) // <3>
    }

    @Test
    fun signedInUsersSeeEmployeesAndSalarySummary() {
        val client = httpClient.toBlocking()
        val html = client.retrieve(HttpRequest.GET<Any>("/").accept(MediaType.TEXT_HTML).basicAuth(USERNAME, PASSWORD)) // <4>

        assertTrue(html.contains("Signed in as <span class=\"fw-semibold\">$USERNAME</span>"))
        assertTrue(html.contains("href=\"/logout\""))
        assertTrue(html.contains("9826.00")) // <5>
        for (firstName in listOf("Victoria", "Marvin", "Chris", "Emma", "Taylor")) {
            assertTrue(html.contains(firstName)) // <6>
        }
    }

    @Requires(property = "spec.name", value = "HomeControllerTest") // <7>
    @Singleton
    class BasicAuthProvider<B : Any> : HttpRequestAuthenticationProvider<B> { // <8>

        override fun authenticate(
            requestContext: HttpRequest<B>?,
            authRequest: AuthenticationRequest<String, String>
        ): AuthenticationResponse {
            if (USERNAME == authRequest.identity && PASSWORD == authRequest.secret) {
                return AuthenticationResponse.success(USERNAME, mapOf<String, Any>("preferred_username" to USERNAME)) // <9>
            }
            return AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)
        }
    }

    companion object {
        private const val USERNAME = "emma@supremo.onmicrosoft.com"
        private const val PASSWORD = "secret"
    }
}
