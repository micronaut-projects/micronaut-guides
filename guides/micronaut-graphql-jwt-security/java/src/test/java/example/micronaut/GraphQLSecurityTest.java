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

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@MicronautTest
@SuppressWarnings("unchecked")
class GraphQLSecurityTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void anonymousCurrentUserReturnsNull() {
        Map<String, Object> body = execute("{ currentUser { username } }");
        Map<String, Object> data = (Map<String, Object>) body.get("data");
        assertNull(data.get("currentUser"));
    }

    @Test
    void loginSetsCookieAndAllowsCurrentUserQuery() {
        HttpResponse<Map<String, Object>> loginResponse = exchange("mutation { login(username:\\\"admin\\\", password:\\\"password\\\") { user { username roles } error } }");
        String cookie = loginResponse.getHeaders().get("Set-Cookie");
        assertNotNull(cookie);

        Map<String, Object> loginData = (Map<String, Object>) loginResponse.body().get("data");
        Map<String, Object> loginPayload = (Map<String, Object>) loginData.get("login");
        Map<String, Object> user = (Map<String, Object>) loginPayload.get("user");
        assertEquals("admin", user.get("username"));
        assertNull(loginPayload.get("error"));

        HttpRequest<String> currentUserRequest = HttpRequest.POST(
                "/graphql",
                "{\"query\":\"{ currentUser { username firstName lastName roles } }\"}"
        ).header("Cookie", cookie.split(";", 2)[0]);

        HttpResponse<Map<String, Object>> currentUserResponse = client.toBlocking().exchange(
                currentUserRequest,
                Argument.mapOf(String.class, Object.class)
        );

        Map<String, Object> currentUserData = (Map<String, Object>) currentUserResponse.body().get("data");
        Map<String, Object> currentUser = (Map<String, Object>) currentUserData.get("currentUser");
        assertEquals("admin", currentUser.get("username"));
        assertEquals("Admin", currentUser.get("firstName"));
    }

    @Test
    void failedLoginReturnsError() {
        Map<String, Object> body = execute("mutation { login(username:\\\"admin\\\", password:\\\"wrong\\\") { user { username } error } }");
        Map<String, Object> data = (Map<String, Object>) body.get("data");
        Map<String, Object> loginPayload = (Map<String, Object>) data.get("login");
        assertNull(loginPayload.get("user"));
        assertNotNull(loginPayload.get("error"));
    }

    private Map<String, Object> execute(String graphql) {
        return exchange(graphql).body();
    }

    private HttpResponse<Map<String, Object>> exchange(String graphql) {
        String query = """
        { "query": "%s" }
        """.formatted(graphql);

        HttpResponse<Map<String, Object>> response = client.toBlocking().exchange(
                HttpRequest.POST("/graphql", query),
                Argument.mapOf(String.class, Object.class)
        );
        assertEquals(HttpStatus.OK, response.status());
        return response;
    }
}
