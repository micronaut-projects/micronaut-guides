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
package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName
import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

@MicronautTest // <1>
class MicronautguideTest extends Specification implements TestPropertyProvider { // <3>
    private static DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:latest")
    @AutoCleanup
    @Shared
    private static LocalStackContainer localstack = new LocalStackContainer(localstackImage)
            .withServices(LocalStackContainer.Service.SQS)

    @Override
    @NonNull
    Map<String, String> getProperties() {
        if (!localstack.isRunning()) {
            localstack.start()
        }
        Map.of("aws.access-key-id", localstack.accessKey,
                "aws.secret-key", localstack.secretKey,
                "aws.region", localstack.region,
                "aws.services.sqs.endpoint-override", localstack.getEndpointOverride(LocalStackContainer.Service.SQS).toString())
    }

    @Inject
    @Client("/")
    HttpClient httpClient

    @Inject
    DemoConsumer demoConsumer

    void "verify jms consumes message"() {
        when:
        httpClient.toBlocking().exchange(HttpRequest.POST("/demo", Collections.emptyMap()))
        then:
        new PollingConditions().eventually {
            assert 1 == demoConsumer.messageCount
        }
    }
}
