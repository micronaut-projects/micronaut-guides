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
/*
//tag::package[]
package example.micronaut
//tag::package[]
*/
//tag::imports[]

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.UUID

@MicronautTest // <1>
class RequestScopeTest(@Client("/") val httpClient: HttpClient) { // <2>
// end::imports[]

    // tag::test[]
    @ParameterizedTest
    @ValueSource(strings = ["/request"])
    fun requestScopeScopeIsACustomScopeThatIndicatesANewInstanceOfTheBeanIsCreatedAndAssociatedWithEachHTTPRequest(path: String) {
        val responses = executeRequest(httpClient, createRequest(path)).toMutableSet()
        assertEquals(1, responses.size) // <3>
        responses.addAll(executeRequest(httpClient, createRequest(path)))
        assertEquals(2, responses.size) // <4>
    }

    private fun executeRequest(client: HttpClient, request: HttpRequest<Any>): List<String> {
        return client.toBlocking().retrieve(
            request,
            Argument.listOf(String::class.java)
        )
    }

    private fun createRequest(path: String): HttpRequest<Any> {
        return HttpRequest.GET<Any>(path).header("UUID", UUID.randomUUID().toString())
    }
}
//end::test[]