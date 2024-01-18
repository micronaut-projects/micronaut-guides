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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;
import java.util.Map;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
class MicronautguideTest implements TestPropertyProvider { // <3>
    private static DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:latest");
    private static LocalStackContainer localstack = new LocalStackContainer(localstackImage)
            .withServices(LocalStackContainer.Service.SQS);

    @Override
    public @NonNull Map<String, String> getProperties() {
        if (!localstack.isRunning()) {
            localstack.start();
        }
        return Map.of("aws.access-key-id", localstack.getAccessKey(),
                "aws.secret-key", localstack.getSecretKey(),
                "aws.region", localstack.getRegion(),
                "aws.services.sqs.endpoint-override", localstack.getEndpointOverride(LocalStackContainer.Service.SQS).toString());
    }
    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    DemoConsumer demoConsumer;

    @Test
    void testItWorks() {
        assertEquals(0, demoConsumer.getMessageCount());
        httpClient.toBlocking().exchange(HttpRequest.POST("/demo", Collections.emptyMap()));
        await().until(() -> demoConsumer.getMessageCount(), equalTo(1));
        assertEquals(1, demoConsumer.getMessageCount());
    }
}
