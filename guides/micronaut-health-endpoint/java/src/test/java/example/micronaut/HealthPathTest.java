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
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import io.micronaut.context.annotation.Property;
import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Property(name = "endpoints.all.path", value = "/endpoints/") // <1>
@MicronautTest
public class HealthPathTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void healthEndpointExposedAtNonDefaultEndpointsPath() {
        HttpStatus status = client.toBlocking().retrieve(HttpRequest.GET("/endpoints/health"), HttpStatus.class); // <2>
        assertEquals(HttpStatus.OK, status);

        Executable e = () -> client.toBlocking().retrieve(HttpRequest.GET("/health"), HttpStatus.class);
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, e);
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus()); // <3>
    }
}
