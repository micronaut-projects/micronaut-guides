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
import net.minidev.json.JSONArray
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class SaasSubscriptionGetListControllerTest {

    @LocalServerPort // <2>
    var port: Int = 0

    lateinit var restClient: RestTestClient // <3>

    @BeforeEach
    fun setUp() {
        restClient = RestTestClient.bindToServer()
            .baseUrl("http://localhost:$port")
            .build()
    }

    @Test
    fun shouldReturnASortedPageOfSaasSubscriptions() {
        var response = restClient.get()
            .uri("/subscriptions?page=0&size=1&sort=cents,desc")
            .exchange()
            .returnResult(String::class.java)
        assertThat(response.status).isEqualTo(HttpStatus.OK)

        var documentContext = JsonPath.parse(response.responseBody)
        val read: JSONArray = documentContext.read("$[*]")
        assertThat(read.size).isEqualTo(1)

        var cents: Int? = documentContext.read("$[0].cents")
        assertThat(cents).isEqualTo(4900)

        response = restClient.get()
            .uri("/subscriptions?page=0&size=1")
            .exchange()
            .returnResult(String::class.java)
        documentContext = JsonPath.parse(response.responseBody)
        cents = documentContext.read("$[0].cents")
        assertThat(cents).isEqualTo(1400)
    }

    @Test
    fun shouldReturnAPageOfSaasSubscriptions() {
        val response = restClient.get()
            .uri("/subscriptions?page=0&size=1")
            .exchange()
            .returnResult(String::class.java)
        assertThat(response.status).isEqualTo(HttpStatus.OK)

        val documentContext = JsonPath.parse(response.responseBody)
        val page: JSONArray = documentContext.read("$[*]")
        assertThat(page.size).isEqualTo(1)
    }

    @Test
    fun shouldReturnAllSaasSubscriptionsWhenListIsRequested() {
        val response = restClient.get()
            .uri("/subscriptions")
            .exchange()
            .returnResult(String::class.java)
        assertThat(response.status).isEqualTo(HttpStatus.OK)

        val documentContext = JsonPath.parse(response.responseBody)
        val saasSubscriptionCount: Int = documentContext.read("$.length()")
        assertThat(saasSubscriptionCount).isEqualTo(3)

        val ids: JSONArray = documentContext.read("$..id")
        assertThat(ids).containsExactlyInAnyOrder(99, 100, 101)

        val cents: JSONArray = documentContext.read("$..cents")
        assertThat(cents).containsExactlyInAnyOrder(1400, 2900, 4900)
    }
}
