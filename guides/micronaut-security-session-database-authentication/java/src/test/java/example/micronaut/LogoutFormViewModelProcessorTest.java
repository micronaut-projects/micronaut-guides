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

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.filters.AuthenticationFetcher;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Disabled;

@Property(name = "spec.name", value = "LogoutFormViewModelProcessorTest")
@MicronautTest
class LogoutFormViewModelProcessorTest {

    @Disabled
    @Test
    void logoutForm(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        String html = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET("/").accept(MediaType.TEXT_HTML)));
        assertNotNull(html);
        assertTrue(html.contains("action=\"/logout\""));
    }

    @Requires(property = "spec.name", value = "LogoutFormViewModelProcessorTest")
    @Singleton
    static class MockAuthenticationFetcher implements AuthenticationFetcher<HttpRequest<?>> {

        @Override
        public Publisher<Authentication> fetchAuthentication(HttpRequest<?> request) {
            return Publishers.just(Authentication.build("admin"));
        }
    }

}