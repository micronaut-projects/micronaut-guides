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
import org.springframework.test.web.servlet.client.ExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class SaasSubscriptionPostControllerTest {

    @LocalServerPort
    private int port; // <2>

    private RestTestClient restTestClient;  // <3>

    @BeforeEach
    void setUp() {
        this.restTestClient = RestTestClient.bindToServer()
                .baseUrl("http://localhost:" + this.port)
                .build();
    }

    @Test
    void shouldCreateANewSaasSubscription() {
        SaasSubscription newSaasSubscription = new SaasSubscription(null, "Advanced", 2500);
        ExchangeResult createResponse = restTestClient.post()
                .uri("/subscriptions")
                .body(newSaasSubscription)
                .exchange()
                .returnResult();
        assertThat(createResponse.getStatus()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewSaasSubscription = createResponse.getResponseHeaders().getLocation();
        ExchangeResult getResponse = restTestClient.get()
                .uri(locationOfNewSaasSubscription.toString())
                .exchange()
                .returnResult();
        assertThat(getResponse.getStatus()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(
                new String(getResponse.getResponseBodyContent(), StandardCharsets.UTF_8));
        Number id = documentContext.read("$.id");
        assertThat(id).isNotNull();

        Integer cents = documentContext.read("$.cents");
        assertThat(cents).isEqualTo(2500);
    }
}
