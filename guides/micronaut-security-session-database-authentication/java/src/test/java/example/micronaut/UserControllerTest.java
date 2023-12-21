/*
 * Copyright 2017-2023 original authors
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

import example.micronaut.repositories.RoleJdbcRepository;
import example.micronaut.repositories.UserJdbcRepository;
import example.micronaut.repositories.UserRoleJdbcRepository;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(transactional = false)
class UserControllerTest {
    @Test
    void signupForm(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = UriBuilder.of("/user").path("signUp").build();
        String html = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET(uri).accept(MediaType.TEXT_HTML)));
        assertNotNull(html);
    }

    @Test
    void loginForm(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = UriBuilder.of("/user").path("auth").build();
        String html = assertDoesNotThrow(() -> client.retrieve(HttpRequest.GET(uri).accept(MediaType.TEXT_HTML)));
        assertNotNull(html);
    }

    @Test
    void successfulSignup(@Client("/") HttpClient httpClient,
                          UserJdbcRepository userRepository,
                          UserRoleJdbcRepository userRoleRepository,
                          RoleJdbcRepository roleRepository) {
        BlockingHttpClient client = httpClient.toBlocking();
        URI uri = UriBuilder.of("/user").path("signUp").build();
        String html = assertDoesNotThrow(() -> client.retrieve(HttpRequest.POST(uri, Map.of("username", "admin",
                "password", "admin123",
                "repeatPassword", "admin123")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.TEXT_HTML)));
        assertNotNull(html);
        assertTrue(html.contains("Username"));
        assertTrue(html.contains("Password"));
        assertFalse(html.contains("Repeat"));

        // Attempting to signup with same username
        HttpClientResponseException ex = assertThrows(HttpClientResponseException.class, () -> client.retrieve(HttpRequest.POST(uri, Map.of("username", "admin",
                        "password", "admin123",
                        "repeatPassword", "admin123")).contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.TEXT_HTML)));
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatus());
        html = ex.getResponse().getBody(String.class).orElse(null);
        assertNotNull(html);
        assertTrue(html.contains("Username"));
        assertTrue(html.contains("Password"));
        assertTrue(html.contains("Repeat"));

        userRoleRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

}
