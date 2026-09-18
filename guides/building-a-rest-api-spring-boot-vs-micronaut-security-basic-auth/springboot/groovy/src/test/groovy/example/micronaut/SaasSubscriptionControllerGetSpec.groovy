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

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.web.servlet.client.EntityExchangeResult
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class SaasSubscriptionControllerGetSpec {

    @LocalServerPort // <2>
    int port

    RestTestClient restTestClient // <3>

    @BeforeEach
    void setUp() {
        restTestClient = RestTestClient.bindToServer()
                .baseUrl("http://localhost:$port")
                .build()
    }

    @Test
    void shouldReturnASaasSubscriptionWhenDataIsSaved() {
        EntityExchangeResult<String> response = withBasicAuth("sarah1", "abc123")
                .get()
                .uri("/subscriptions/99")
                .exchange()
                .returnResult(String)

        assert response.status == HttpStatus.OK

        DocumentContext documentContext = JsonPath.parse(response.responseBody)
        Number id = documentContext.read('$.id')
        assert id != null
        assert id == 99

        String name = documentContext.read('$.name')
        assert name != null
        assert name == "Advanced"

        Integer cents = documentContext.read('$.cents')
        assert cents == 2900
    }

    @Test
    void shouldNotReturnASaasSubscriptionWithAnUnknownId() {
        EntityExchangeResult<String> response = withBasicAuth("sarah1", "abc123")
                .get()
                .uri("/subscriptions/1000")
                .exchange()
                .returnResult(String)
        assert response.status == HttpStatus.NOT_FOUND
        assert (response.responseBody ?: "").blank
    }

    private RestTestClient withBasicAuth(String username, String password) {
        restTestClient.mutate()
                .defaultHeaders { headers -> headers.setBasicAuth(username, password) }
                .build()
    }
}
