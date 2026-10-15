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

import com.jayway.jsonpath.JsonPath
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.net.URI

@MicronautTest // <1>
class SaasSubscriptionPostControllerTest {

    @Test
    fun shouldCreateANewSaasSubscription(@Client("/") httpClient: HttpClient) { // <2>
        val client = httpClient.toBlocking()
        val subscription = SaasSubscription(100, "Advanced", 2900)

        val createResponse = client.exchange(HttpRequest.POST("/subscriptions", subscription), Void::class.java)
        assertThat(createResponse.status.code).isEqualTo(HttpStatus.CREATED.code)
        val locationOfNewSaasSubscriptionOptional = createResponse.headers.get(HttpHeaders.LOCATION, URI::class.java)
        assertThat(locationOfNewSaasSubscriptionOptional).isPresent()

        val locationOfNewSaasSubscription = locationOfNewSaasSubscriptionOptional.get()
        val getResponse = client.exchange(HttpRequest.GET<Any>(locationOfNewSaasSubscription), String::class.java)
        assertThat(getResponse.status.code).isEqualTo(HttpStatus.OK.code)

        val documentContext = JsonPath.parse(getResponse.body())
        val id: Number? = documentContext.read("$.id")
        assertThat(id).isNotNull()

        val cents: Int? = documentContext.read("$.cents")
        assertThat(cents).isEqualTo(2900)
    }
}
