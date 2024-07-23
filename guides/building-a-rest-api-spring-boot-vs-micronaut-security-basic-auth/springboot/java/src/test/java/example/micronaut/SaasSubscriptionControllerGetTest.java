/*
 * Copyright 2017-2024 original authors
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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // <1>
class SaasSubscriptionControllerGetTest {

    @Autowired // <2>
    TestRestTemplate restTemplate; // <3>

    @Test
    void shouldReturnASaasSubscriptionWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/subscriptions/99", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isNotNull();
        assertThat(id).isEqualTo(99);

        String name = documentContext.read("$.name");
        assertThat(name).isNotNull();
        assertThat(name).isEqualTo("Advanced");

        Integer cents = documentContext.read("$.cents");
        assertThat(cents).isEqualTo(2900);
    }

    @Test
    void shouldNotReturnASaasSubscriptionWithAnUnknownId() {
        ResponseEntity<String> response = restTemplate
                .withBasicAuth("sarah1", "abc123")
                .getForEntity("/subscriptions/1000", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isBlank();
    }
}
