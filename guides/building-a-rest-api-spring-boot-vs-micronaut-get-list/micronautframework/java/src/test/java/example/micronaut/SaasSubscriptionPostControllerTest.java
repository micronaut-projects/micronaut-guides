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
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest // <1>
class SaasSubscriptionPostControllerTest {

    @Test
    void shouldCreateANewSaasSubscription(@Client("/") HttpClient httpClient) { // <2>
        BlockingHttpClient client = httpClient.toBlocking();
        SaasSubscription subscription = new SaasSubscription(100L, "Advanced", 2900);

        HttpResponse<Void> createResponse = client.exchange(HttpRequest.POST("/subscriptions", subscription), Void.class);
        assertThat(createResponse.getStatus().getCode()).isEqualTo(HttpStatus.CREATED.getCode());
        Optional<URI> locationOfNewSaasSubscriptionOptional = createResponse.getHeaders().get(HttpHeaders.LOCATION, URI.class);
        assertThat(locationOfNewSaasSubscriptionOptional).isPresent();

        URI locationOfNewSaasSubscription = locationOfNewSaasSubscriptionOptional.get();
        HttpResponse<String> getResponse = client.exchange(HttpRequest.GET(locationOfNewSaasSubscription), String.class);
        assertThat(getResponse.getStatus().getCode()).isEqualTo(HttpStatus.OK.getCode());

        DocumentContext documentContext = JsonPath.parse(getResponse.body());
        Number id = documentContext.read("$.id");
        assertThat(id).isNotNull();

        Integer cents = documentContext.read("$.cents");
        assertThat(cents).isEqualTo(2900);
    }
}