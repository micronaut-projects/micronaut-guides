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
import io.micronaut.context.annotation.Property;
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
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.CreateSecretRequest;

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Property(name = "spec.name", value = "ClientIdControllerTest")
@MicronautTest // <1>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <2>
class ClientIdControllerTest implements TestPropertyProvider { // <3>

    private static final DockerImageName FLOCI_IMAGE = DockerImageName.parse("floci/floci:1.5.18");
    private static FlociContainer floci = new FlociContainer(FLOCI_IMAGE);

    @Override
    public @NonNull Map<String, String> getProperties() {
        if (!floci.isRunning()) {
            floci.start();
        }
        try (SecretsManagerClient secretsManager = SecretsManagerClient.builder()
                .endpointOverride(URI.create(floci.getEndpoint()))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(floci.getAccessKey(), floci.getSecretKey())))
                .build()
        ) {
            secretsManager.createSecret(CreateSecretRequest.builder()
                    .name("/config/micronautguide/companyserveroauth")
                    .secretString("{\"micronaut.security.oauth2.clients.companyauthserver.client-id\":\"XXX\",\"micronaut.security.oauth2.clients.companyauthserver.client-secret\":\"YYY\"}")
                    .build());
        }
        return Map.of("aws.access-key-id", floci.getAccessKey(),
                "aws.secret-key", floci.getSecretKey(),
                "aws.region", floci.getRegion(),
                "aws.services.secretsmanager.endpoint-override", floci.getEndpoint());
    }

    @Test
    void testSecretsManager(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        String rsp = assertDoesNotThrow(() -> client.retrieve("/"));
        assertEquals("XXX", rsp);
    }
}
