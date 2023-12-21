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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;
import java.util.Map;


@Testcontainers(disabledWithoutDocker = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MicronautTest
class MicronautguideTest implements TestPropertyProvider {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    DemoConsumer demoConsumer;

    @Test
    void testItWorks() {
        int messageCount = demoConsumer.getMessageCount();
        Assertions.assertTrue(messageCount == 0);

        httpClient.toBlocking().exchange(HttpRequest.POST("/demo", Collections.emptyMap()));
        messageCount = demoConsumer.getMessageCount();
        while (messageCount == 0) {
            messageCount = demoConsumer.getMessageCount();
        }

        Assertions.assertTrue(messageCount == 1);
    }

    @AfterAll
    static void afterAll() {
        LocalStack.close();
    }

    @Override
    @NonNull
    public Map<String, String> getProperties() {
        return LocalStack.getProperties();
    }
}
