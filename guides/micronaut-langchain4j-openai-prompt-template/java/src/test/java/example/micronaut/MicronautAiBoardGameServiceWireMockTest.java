/*
 * Copyright 2017-2025 original authors
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

import com.github.tomakehurst.wiremock.client.WireMock;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.MediaType;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.util.Map;

import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers(disabledWithoutDocker = true)
@Property(name = "langchain4j.open-ai.api-key", value = "xxx") // <1>
@MicronautTest // <2>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <3>
class MicronautAiBoardGameServiceWireMockTest extends WireMockTestPropertyProvider {
    @NonNull
    public Map<String, String> getProperties() {
        return Map.of("langchain4j.open-ai.base-url",
                getWireMockServer().baseUrl());
    }

    @AfterAll
    void cleanupSpec() {
        stopWireMock();
    }

    @Test
    void testAskQuestion(ResourceLoader resourceLoader,
                         BoardGameService boardGameService) throws IOException {
        String jsonString = ResourceUtils.classPathResource(resourceLoader,
                "test-openai-response.json");
        getWireMockServer().stubFor(WireMock.post("/chat/completions")
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                        .withBody(jsonString)));
        var answer = boardGameService.askQuestion(
                new Question("Trivial", "What is the capital of France?"));
        assertNotNull(answer);
        assertTrue(answer.answer().contains("Paris"),
                answer.answer() + " does not contain Paris");
    }
}
