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
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import net.minidev.json.JSONArray
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Sql(value = ["classpath:schema.sql", "classpath:data.sql"]) // <1>
@MicronautTest // <2>
class SaasSubscriptionGetListControllerTest(
    @param:Client("/") private val httpClient: HttpClient // <3>
) {

    @Test
    fun shouldReturnASortedPageOfSaasSubscriptions() {
        val client = httpClient.toBlocking()
        val uri = UriBuilder.of("/subscriptions")
            .queryParam("page", 0)
            .queryParam("size", 1)
            .queryParam("sort", "cents,desc")
            .build()
        val response = client.exchange(HttpRequest.GET<Any>(uri), String::class.java)
        assertThat(response.status.code).isEqualTo(HttpStatus.OK.code)

        val documentContext = JsonPath.parse(response.body())
        val page: JSONArray = documentContext.read("$[*]")
        assertThat(page).hasSize(1)

        val cents: Int? = documentContext.read("$[0].cents")
        assertThat(cents).isEqualTo(4900)
    }

    @Test
    fun shouldReturnAPageOfSaasSubscriptions() {
        val client = httpClient.toBlocking()
        val uri = UriBuilder.of("/subscriptions")
            .queryParam("page", 0)
            .queryParam("size", 1)
            .build()
        val response = client.exchange(HttpRequest.GET<Any>(uri), String::class.java)
        assertThat(response.status.code).isEqualTo(HttpStatus.OK.code)

        val documentContext = JsonPath.parse(response.body())
        val page: JSONArray = documentContext.read("$[*]")
        assertThat(page.size).isEqualTo(1)
    }

    @Test
    fun shouldReturnAllSaasSubscriptionsWhenListIsRequested() {
        val client = httpClient.toBlocking()
        val response = client.exchange("/subscriptions", String::class.java)
        assertThat(response.status.code).isEqualTo(HttpStatus.OK.code)

        val documentContext = JsonPath.parse(response.body())
        val saasSubscriptionCount: Int = documentContext.read("$.length()")
        assertThat(saasSubscriptionCount).isEqualTo(3)

        val ids: JSONArray = documentContext.read("$..id")
        assertThat(ids).containsExactlyInAnyOrder(99, 100, 101)

        val cents: JSONArray = documentContext.read("$..cents")
        assertThat(cents).containsExactlyInAnyOrder(1400, 2900, 4900)
    }
}
