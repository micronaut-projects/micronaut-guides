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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class SaasSubscriptionControllerGetTest {

    @LocalServerPort // <2>
    var port: Int = 0

    private lateinit var restClient: RestTestClient // <3>

    @BeforeEach
    fun setUp() {
        restClient = RestTestClient.bindToServer()
            .baseUrl("http://localhost:$port")
            .build()
    }

    @Test
    fun shouldReturnASaasSubscriptionWhenDataIsSaved() {
        val response = restClient.get()
            .uri("/subscriptions/99")
            .exchange()
            .returnResult(String::class.java)

        assertThat(response.status).isEqualTo(HttpStatus.OK)

        val documentContext = JsonPath.parse(response.responseBody)
        val id: Number = documentContext.read("$.id")
        assertThat(id).isNotNull()
        assertThat(id).isEqualTo(99)

        val name: String = documentContext.read("$.name")
        assertThat(name).isNotNull()
        assertThat(name).isEqualTo("Advanced")

        val cents: Int = documentContext.read("$.cents")
        assertThat(cents).isEqualTo(2900)
    }

    @Test
    fun shouldNotReturnASaasSubscriptionWithAnUnknownId() {
        val response = restClient.get()
            .uri("/subscriptions/1000")
            .exchange()
            .returnResult(String::class.java)
        assertThat(response.status).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(response.responseBody).isBlank()
    }
}
