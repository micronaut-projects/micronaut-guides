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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class SaasSubscriptionControllerGetTest {

    @LocalServerPort // <2>
    int port;

    RestTestClient restTestClient; // <3>

    @BeforeEach
    void setUp() {
        restTestClient = RestTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void shouldReturnASaasSubscriptionWhenDataIsSaved() {
        RestTestClient.ResponseSpec response = restTestClient.get()
                .uri("/subscriptions/99")
                .exchange();

        response.expectStatus().isOk();
        response.expectBody(SaasSubscription.class)
                .value(subscription -> assertThat(subscription)
                        .isEqualTo(new SaasSubscription(99L, "Advanced", 2900)));
    }

    @Test
    void shouldNotReturnASaasSubscriptionWithAnUnknownId() {
        RestTestClient.ResponseSpec response = restTestClient.get()
                .uri("/subscriptions/1000")
                .exchange();

        response.expectStatus().isNotFound();
        response.expectBody().isEmpty();
    }
}
