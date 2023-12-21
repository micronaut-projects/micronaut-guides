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
import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE)
@Property(name = "micronaut.security.reject-not-found", value = StringUtils.FALSE)
@MicronautTest(transactional = false)
class LoginTest {

    @Test
    void logins(@Client("/") HttpClient httpClient,
                  RegisterService registerService,
                  UserJdbcRepository userRepository,
                  UserRoleJdbcRepository userRoleRepository,
                  RoleJdbcRepository roleRepository) {
        String username = "admin";
        assertDoesNotThrow(() -> registerService.register(username, "admin123", Arrays.asList("ROLE_USER", "ROLE_ADMIN")));

        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = createRequest("admin", "admin123");
        HttpResponse<String> response = assertDoesNotThrow(() -> client.exchange(request));
        assertNotNull(response.getHeaders().get(HttpHeaders.LOCATION));
        assertEquals("/", response.getHeaders().get(HttpHeaders.LOCATION));

        response = assertDoesNotThrow(() -> client.exchange(createRequest("admin", "wrong")));
        assertNotNull(response.getHeaders().get(HttpHeaders.LOCATION));
        assertEquals("/user/authFailed", response.getHeaders().get(HttpHeaders.LOCATION));

        userRoleRepository.deleteAll();
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

    private HttpRequest<?> createRequest(String username, String password) {
        return HttpRequest.POST("/login", Map.of("username", username, "password", password))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);
    }
}
