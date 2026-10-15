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
import io.micronaut.context.annotation.Requires
import io.micronaut.core.async.publisher.Publishers
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.filters.AuthenticationFetcher
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.function.ThrowingSupplier
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.reactivestreams.Publisher

@Property(name = "spec.name", value = "PlanControllerAuthenticatedTest") // <1>
@MicronautTest // <2>
class PlanControllerAuthenticatedTest(@Client("/") val httpClient: HttpClient) { // <3>

    @Test
    fun planControllerReturnsDefaultPlanForAuthenticatedUser() {
        val client = httpClient.toBlocking()
        val response = assertDoesNotThrow(ThrowingSupplier {
            client.retrieve(HttpRequest.GET<String>("/plan").accept(MediaType.TEXT_PLAIN))
        })
        assertEquals("Plan New Year", response)
    }

    @Requires(property = "spec.name", value = "PlanControllerAuthenticatedTest") // <1>
    @Singleton
    class MockAuthenticationFetcher : AuthenticationFetcher<HttpRequest<*>> {

        override fun fetchAuthentication(request: HttpRequest<*>?): Publisher<Authentication> =
            Publishers.just(Authentication.build("watson"))
    }
}
