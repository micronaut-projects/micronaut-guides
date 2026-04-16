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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class SaasSubscriptionPostControllerTest {

    @LocalServerPort // <2>
    int port;

    RestTestClient restClient;  // <3>

    @BeforeEach
    void setUp() {
        this.restClient = RestTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void shouldCreateANewSaasSubscription() {
        SaasSubscription newSaasSubscription = new SaasSubscription(null, "Advanced", 2500);
        EntityExchangeResult<Void> createResponse = restClient.post()
                .uri("/subscriptions")
                .body(newSaasSubscription)
                .exchange()
                .expectBody()
                .isEmpty();
        assertThat(createResponse.getStatus()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewSaasSubscription = createResponse.getResponseHeaders().getLocation();
        EntityExchangeResult<String> getResponse = restClient.get()
                .uri(locationOfNewSaasSubscription)
                .exchange()
                .returnResult(String.class);
        assertThat(getResponse.getStatus()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(getResponse.getResponseBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isNotNull();

        Integer cents = documentContext.read("$.cents");
        assertThat(cents).isEqualTo(2500);
    }
}
