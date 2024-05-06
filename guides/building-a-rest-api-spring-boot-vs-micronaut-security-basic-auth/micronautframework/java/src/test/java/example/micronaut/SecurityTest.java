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

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

@MicronautTest // <1>
class SecurityTest {
    @Test
    void shouldNotAllowAccessToSaasSubscriptionsTheyDoNotOwn(@Client("/") HttpClient httpClient) { // <2>
        HttpRequest<?> request = HttpRequest.GET("/subscriptions/102")
                .basicAuth("sarah1", "abc123");
        HttpClientResponseException thrown = catchThrowableOfType(() ->
                httpClient.toBlocking().exchange(request, String.class), HttpClientResponseException.class);  // <3>
        assertThat(thrown.getStatus().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    void shouldRejectUsersWhoAreNotSubscriptionOwners(@Client("/") HttpClient httpClient) { // <2>
        HttpRequest<?> badPasswordRequest = HttpRequest.GET("/subscriptions/99")
                .basicAuth("john-owns-no-subscriptions", "qrs456");
        HttpClientResponseException badPasswordEx = catchThrowableOfType(() ->
                httpClient.toBlocking().exchange(badPasswordRequest, String.class), HttpClientResponseException.class);  // <3>
        assertThat(badPasswordEx.getStatus().getCode()).isEqualTo(HttpStatus.FORBIDDEN.getCode());
    }

    @Test
    void shouldNotReturnASaasSubscriptionWithAnUnknownId(@Client("/") HttpClient httpClient) { // <2>
        HttpRequest<?> request = HttpRequest.GET("/subscriptions/1000")
                .basicAuth("sarah1", "BAD-PASSWORD");
        HttpClientResponseException ex = catchThrowableOfType(() ->
                httpClient.toBlocking().exchange(request, String.class), HttpClientResponseException.class);  // <3>
        assertThat(ex.getStatus().getCode()).isEqualTo(HttpStatus.UNAUTHORIZED.getCode());
    }
}
