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

import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "endpoints.health.disk-space.threshold", value = "999999999999999999") // <1>
@MicronautTest
public class PoorHealthTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void healthEndpointExposesOutOfDiscSpace() {

        Executable e = () -> client.toBlocking().retrieve(HttpRequest.GET("/health"));
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, e);

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, thrown.getStatus());  // <2>
        assertTrue(thrown.getResponse().getBody(String.class).orElse("").contains("DOWN"));  // <3>
    }
}




