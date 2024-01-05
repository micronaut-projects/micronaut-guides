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

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelloWorldControllerTest {

    @Test
    void emulateCheckpoint() {
        CheckpointTestUtils.test(this::testHelloWorld);
    }

    private Void testHelloWorld(BlockingHttpClient client) {
        HttpResponse<Map<String, String>> response = client.exchange(HttpRequest.GET("/"), Argument.mapOf(String.class, String.class));
        assertEquals(HttpStatus.OK, response.getStatus());
        Optional<Map<String, String>> bodyOptional = response.getBody();
        assertTrue(bodyOptional.isPresent());
        assertEquals(Collections.singletonMap("message", "Hello World"), bodyOptional.get());
        return null;
    }
}