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
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.client.RestTestClient
import java.net.URI

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class SaasSubscriptionPostControllerTest {

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
    fun shouldCreateANewSaasSubscription() {
        val newSaasSubscription = SaasSubscription(null, "Advanced", 2500)
        val createResponse = restClient.post()
            .uri("/subscriptions")
            .body(newSaasSubscription)
            .exchange()
            .expectBody()
            .isEmpty()
        assertThat(createResponse.status).isEqualTo(HttpStatus.CREATED)

        val locationOfNewSaasSubscription: URI? = createResponse.responseHeaders.location
        assertThat(locationOfNewSaasSubscription).isNotNull()
        val getResponse = restClient.get()
            .uri(locationOfNewSaasSubscription!!)
            .exchange()
            .returnResult(String::class.java)
        assertThat(getResponse.status).isEqualTo(HttpStatus.OK)

        val documentContext = JsonPath.parse(getResponse.responseBody)
        val id: Number? = documentContext.read("$.id")
        assertThat(id).isNotNull()

        val cents: Int? = documentContext.read("$.cents")
        assertThat(cents).isEqualTo(2500)
    }
}
