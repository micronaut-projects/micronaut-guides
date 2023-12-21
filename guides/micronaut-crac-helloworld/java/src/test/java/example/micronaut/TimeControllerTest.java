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

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.crac.test.CheckpointSimulator;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TimeControllerTest {

    @Test
    void emulateCheckpoint() {
        try (EmbeddedServer server = ApplicationContext.run(EmbeddedServer.class)) {
            CheckpointSimulator checkpointSimulator =
                    server.getApplicationContext().getBean(CheckpointSimulator.class);
            Object time = CheckpointTestUtils.testApp(server, this::testTime);
            assertEquals(time, CheckpointTestUtils.testApp(server, this::testTime));
            checkpointSimulator.runBeforeCheckpoint();
            server.stop();
            checkpointSimulator.runAfterRestore();
            server.start();
            assertNotEquals(time, CheckpointTestUtils.testApp(server, this::testTime));
        }
    }

    private String testTime(BlockingHttpClient client) {
        HttpResponse<Map<String, String>> response = client.exchange(HttpRequest.GET("/time"), Argument.mapOf(String.class, String.class));
        assertEquals(HttpStatus.OK, response.getStatus());
        Optional<Map<String, String>> bodyOptional = response.getBody();
        assertTrue(bodyOptional.isPresent());
        Map<String, String> body = bodyOptional.get();
        assertEquals(1, body.keySet().size() );
        assertTrue(body.containsKey("time"));
        return body.get("time");
    }
}