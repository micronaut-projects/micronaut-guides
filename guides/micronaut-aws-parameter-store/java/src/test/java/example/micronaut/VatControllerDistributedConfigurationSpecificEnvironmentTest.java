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
import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.ParameterType;
import software.amazon.awssdk.services.ssm.model.PutParameterRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(environments = {Environment.AMAZON_EC2, "ch"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Property(name = "aws.distributed-configuration.search-active-environments", value = StringUtils.TRUE)
class VatControllerDistributedConfigurationSpecificEnvironmentTest implements TestPropertyProvider {

    private static DockerImageName localstackImage = DockerImageName.parse("localstack/localstack:latest");
    private static LocalStackContainer localstack = new LocalStackContainer(localstackImage)
            .withServices(LocalStackContainer.Service.SSM);

    @Override
    public @NonNull Map<String, String> getProperties() {
        if (!localstack.isRunning()) {
            localstack.start();
        }
        try (SsmClient ssmClient = SsmClient.builder()
                .endpointOverride(localstack.getEndpointOverride(LocalStackContainer.Service.SSM))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey())))
                .build()
        ) {
            ssmClient.putParameter(putParameterRequest("/config/micronautguide/vat/country/", "Spain"));
            ssmClient.putParameter(putParameterRequest("/config/micronautguide/vat/rate/", "21"));
            ssmClient.putParameter(putParameterRequest("/config/micronautguide_ch/vat/country/", "Switzerland"));
            ssmClient.putParameter(putParameterRequest("/config/micronautguide_ch/vat/rate/", "7.7"));
        }
        return Map.of("aws.access-key-id", localstack.getAccessKey(),
                "aws.secret-key", localstack.getSecretKey(),
                "aws.region", localstack.getRegion(),
                "aws.services.ssm.endpoint-override", localstack.getEndpointOverride(LocalStackContainer.Service.SSM).toString());
    }

    private PutParameterRequest putParameterRequest(String name, String value) {
        return PutParameterRequest.builder()
                .name(name)
                .type(ParameterType.STRING)
                .value(value)
                .build();
    }

    @Test
    void vatExposesTheValueAddedTaxRate(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        assertEquals("{\"rate\":7.7}", assertDoesNotThrow(() -> client.retrieve("/vat")));
    }
}
