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

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider;
import io.micronaut.test.annotation.Sql;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql("classpath:employees-test-data.sql")
@MicronautTest // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HomeControllerTest implements TestPropertyProvider {

    private static final String USERNAME = "emma@supremo.onmicrosoft.com";
    private static final String PASSWORD = "secret";

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Override
    @NonNull
    public Map<String, String> getProperties() {
        Map<String, String> properties = new HashMap<>(Oracle.getProperties());
        properties.put("spec.name", "HomeControllerTest"); // <2>
        properties.put("micronaut.server.port", "-1");
        return properties;
    }

    @Test
    void anonymousUsersAreInvitedToSignIn() {
        BlockingHttpClient client = httpClient.toBlocking();
        String html = client.retrieve(HttpRequest.GET("/").accept(MediaType.TEXT_HTML));

        assertTrue(html.contains("Anonymous"));
        assertTrue(html.contains("Sign in with Microsoft Entra ID"));
        assertTrue(html.contains("href=\"/oauth/login/entraid\"")); // <3>
    }

    @Test
    void signedInUsersSeeEmployeesAndSalarySummary() {
        BlockingHttpClient client = httpClient.toBlocking();
        String html = client.retrieve(HttpRequest.GET("/").accept(MediaType.TEXT_HTML).basicAuth(USERNAME, PASSWORD)); // <4>

        assertTrue(html.contains("Signed in as <span class=\"fw-semibold\">" + USERNAME + "</span>"));
        assertTrue(html.contains("href=\"/logout\""));
        assertTrue(html.contains("9826.00")); // <5>
        for (String firstName : List.of("Victoria", "Marvin", "Chris", "Emma", "Taylor")) {
            assertTrue(html.contains(firstName)); // <6>
        }
    }

    @Requires(property = "spec.name", value = "HomeControllerTest") // <7>
    @Singleton
    static class BasicAuthProvider<B> implements HttpRequestAuthenticationProvider<B> { // <8>

        @Override
        public @NonNull AuthenticationResponse authenticate(@Nullable HttpRequest<B> requestContext,
                                                            @NonNull AuthenticationRequest<String, String> authRequest) {
            if (USERNAME.equals(authRequest.getIdentity()) && PASSWORD.equals(authRequest.getSecret())) {
                return AuthenticationResponse.success(USERNAME, Map.of("preferred_username", USERNAME)); // <9>
            }
            return AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH);
        }
    }
}
