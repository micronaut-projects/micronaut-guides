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
//end::package[]
*/
//tag::imports[]

import io.micronaut.context.annotation.Property
import io.micronaut.core.type.Argument
import io.micronaut.core.util.StringUtils
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@Property(name = "endpoints.refresh.enabled", value = StringUtils.TRUE) // <1>
@Property(name = "endpoints.refresh.sensitive", value = StringUtils.FALSE) // <2>
@MicronautTest // <3>
class RefreshableScopeTest(@Client("/") val httpClient: HttpClient) { // <4>
//end::imports[]

    //tag::test[]
    @ParameterizedTest
    @ValueSource(strings = ["/refreshable"])
    fun refreshableScopeIsACustomScopeThatAllowsABeansStateToBeRefreshedViaTheRefreshEndpoint(path: String) {
        val responses = executeRequest(httpClient, path).toMutableSet()
        assertEquals(1, responses.size) // <5>
        responses.addAll(executeRequest(httpClient, path))
        assertEquals(1, responses.size) // <6>
        refresh() // <7>
        responses.addAll(executeRequest(httpClient, path))
        assertEquals(2, responses.size) // <8>
    }

    private fun executeRequest(client: HttpClient, path: String): List<String> {
        return client.toBlocking().retrieve(
            HttpRequest.GET<Any>(path),
            Argument.listOf(String::class.java)
        )
    }

    private fun refresh() {
        httpClient.toBlocking().exchange<Any, Any>(
            HttpRequest.POST(
                "/refresh",
                mapOf("force" to true)
            )
        )
    }

}
//end::test[]