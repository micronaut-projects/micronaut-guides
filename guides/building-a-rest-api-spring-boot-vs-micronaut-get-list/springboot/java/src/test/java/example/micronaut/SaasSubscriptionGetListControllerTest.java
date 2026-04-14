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
package example.micronaut;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class SaasSubscriptionGetListControllerTest {

    @LocalServerPort // <2>
    int port;

    RestTestClient restClient; // <3>

    @BeforeEach
    void setUp() {
        this.restClient = RestTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void shouldReturnASortedPageOfSaasSubscriptions() {
        EntityExchangeResult<String> response = restClient.get()
                .uri("/subscriptions?page=0&size=1&sort=cents,desc")
                .exchange()
                .returnResult(String.class);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getResponseBody());
        JSONArray read = documentContext.read("$[*]");
        assertThat(read.size()).isEqualTo(1);

        Integer cents = documentContext.read("$[0].cents");
        assertThat(cents).isEqualTo(4900);

        response = restClient.get()
                .uri("/subscriptions?page=0&size=1")
                .exchange()
                .returnResult(String.class);
        documentContext = JsonPath.parse(response.getResponseBody());
        cents = documentContext.read("$[0].cents");
        assertThat(cents).isEqualTo(1400);
    }

    @Test
    void shouldReturnAPageOfSaasSubscriptions() {
        EntityExchangeResult<String> response = restClient.get()
                .uri("/subscriptions?page=0&size=1")
                .exchange()
                .returnResult(String.class);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getResponseBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(1);
    }

    @Test
    void shouldReturnAllSaasSubscriptionsWhenListIsRequested() {
        EntityExchangeResult<String> response = restClient.get()
                .uri("/subscriptions")
                .exchange()
                .returnResult(String.class);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getResponseBody());
        int saasSubscriptionCount = documentContext.read("$.length()");
        assertThat(saasSubscriptionCount).isEqualTo(3);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(99, 100, 101);

        JSONArray cents = documentContext.read("$..cents");
        assertThat(cents).containsExactlyInAnyOrder(1400, 2900, 4900);
    }

}
