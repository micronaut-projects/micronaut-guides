/*
 * Copyright 2017-2026 original authors
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

import io.floci.testcontainers.FlociContainer;
import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.ParameterType;
import software.amazon.awssdk.services.ssm.model.PutParameterRequest;

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(environments = {Environment.AMAZON_EC2, "ch"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VatControllerDistributedConfigurationSpecificEnvironmentTest implements TestPropertyProvider {

    private static final DockerImageName FLOCI_IMAGE = DockerImageName.parse("floci/floci:1.5.18");
    private static FlociContainer floci = new FlociContainer(FLOCI_IMAGE);

    @Override
    public @NonNull Map<String, String> getProperties() {
        if (!floci.isRunning()) {
            floci.start();
        }
        configureSsmProperties();
        try (SsmClient ssmClient = SsmClient.builder()
                .endpointOverride(URI.create(floci.getEndpoint()))
                .region(Region.of(floci.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(floci.getAccessKey(), floci.getSecretKey())))
                .build()
        ) {
            ssmClient.putParameter(putParameterRequest("/config/micronautguide/vat/country", "Spain"));
            ssmClient.putParameter(putParameterRequest("/config/micronautguide/vat/rate", "21"));
            ssmClient.putParameter(putParameterRequest("/config/micronautguide_ch/vat/country", "Switzerland"));
            ssmClient.putParameter(putParameterRequest("/config/micronautguide_ch/vat/rate", "7.7"));
        }
        return Map.of("aws.access-key-id", floci.getAccessKey(),
                "aws.secret-key", floci.getSecretKey(),
                "aws.region", floci.getRegion(),
                "aws.services.ssm.endpoint-override", floci.getEndpoint(),
                "micronaut.config.import", "parameterstore:///config/micronautguide_ch");
    }

    private void configureSsmProperties() {
        System.setProperty("aws.access-key-id", floci.getAccessKey());
        System.setProperty("aws.secret-key", floci.getSecretKey());
        System.setProperty("aws.region", floci.getRegion());
        System.setProperty("aws.services.ssm.endpoint-override", floci.getEndpoint());
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
