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

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Property(name = "endpoints.info.enabled", value = StringUtils.TRUE)
@Property(name = "endpoints.info.sensitive", value = StringUtils.FALSE)
@MicronautTest
class InfoEndpointTest {

    @Test
    void cracInformationIsExposedInTheInfoEndpointExposed(@Client("/") HttpClient client) {
        Map<String, Map<String, Integer>> json = assertDoesNotThrow(() -> client.toBlocking().retrieve(HttpRequest.GET("/info"), Argument.mapOf(Argument.of(String.class), Argument.mapOf(String.class, Integer.class))));
        assertNotNull(json);
        assertEquals(Map.of("crac", Map.of("restore-time", -1, "uptime-since-restore", -1)), json);
    }
}