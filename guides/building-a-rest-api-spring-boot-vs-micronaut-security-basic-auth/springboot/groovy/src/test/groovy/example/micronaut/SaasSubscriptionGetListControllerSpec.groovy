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
import net.minidev.json.JSONArray
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.client.EntityExchangeResult
import org.springframework.test.web.servlet.client.RestTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // <2>
class SaasSubscriptionGetListControllerSpec {

    @LocalServerPort // <3>
    int port

    RestTestClient restTestClient // <4>

    @BeforeEach
    void setUp() {
        restTestClient = RestTestClient.bindToServer()
                .baseUrl("http://localhost:$port")
                .build()
    }

    @Test
    void shouldReturnASortedPageOfSaasSubscriptions() {
        EntityExchangeResult<String> response = withBasicAuth("sarah1", "abc123")
                .get()
                .uri("/subscriptions?page=0&size=1&sort=cents,desc")
                .exchange()
                .returnResult(String)
        assert response.status == HttpStatus.OK

        DocumentContext documentContext = JsonPath.parse(response.responseBody)
        JSONArray read = documentContext.read('$[*]')
        assert read.size() == 1

        Integer cents = documentContext.read('$[0].cents')
        assert cents == 4900

        response = withBasicAuth("sarah1", "abc123")
                .get()
                .uri("/subscriptions?page=0&size=1")
                .exchange()
                .returnResult(String)
        documentContext = JsonPath.parse(response.responseBody)
        cents = documentContext.read('$[0].cents')
        assert cents == 1400
    }

    @Test
    void shouldReturnAPageOfSaasSubscriptions() {
        EntityExchangeResult<String> response = withBasicAuth("sarah1", "abc123")
                .get()
                .uri("/subscriptions?page=0&size=1")
                .exchange()
                .returnResult(String)
        assert response.status == HttpStatus.OK

        DocumentContext documentContext = JsonPath.parse(response.responseBody)
        JSONArray page = documentContext.read('$[*]')
        assert page.size() == 1
    }

    @Test
    void shouldReturnAllSaasSubscriptionsWhenListIsRequested() {
        EntityExchangeResult<String> response = withBasicAuth("sarah1", "abc123")
                .get()
                .uri("/subscriptions")
                .exchange()
                .returnResult(String)
        assert response.status == HttpStatus.OK

        DocumentContext documentContext = JsonPath.parse(response.responseBody)
        int saasSubscriptionCount = documentContext.read('$.length()')
        assert saasSubscriptionCount == 3

        JSONArray ids = documentContext.read('$..id')
        assert ids.containsAll([99, 100, 101])

        JSONArray cents = documentContext.read('$..cents')
        assert cents.containsAll([1400, 2900, 4900])
    }

    private RestTestClient withBasicAuth(String username, String password) {
        restTestClient.mutate()
                .defaultHeaders { headers -> headers.setBasicAuth(username, password) }
                .build()
    }
}
