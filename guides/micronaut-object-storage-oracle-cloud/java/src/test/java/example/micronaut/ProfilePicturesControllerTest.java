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

import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.CreateBucketDetails;
import com.oracle.bmc.objectstorage.requests.CreateBucketRequest;
import com.oracle.bmc.objectstorage.requests.DeleteBucketRequest;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.objectstorage.oraclecloud.OracleCloudStorageConfiguration;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.Map;

import static example.micronaut.ProfilePicturesControllerTest.SPEC_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers(disabledWithoutDocker = true)
@MicronautTest
@Property(name = "spec.name", value = SPEC_NAME)
class ProfilePicturesControllerTest extends AbstractProfilePicturesControllerTest {

    static final String SPEC_NAME = "ProfilePicturesControllerTest";

    static OciEmulatorContainer ociEmulator = new OciEmulatorContainer(OciEmulatorContainer.DEFAULT_IMAGE_NAME);

    @Inject
    ObjectStorage client;

    @Inject
    OracleCloudStorageConfiguration configuration;

    @BeforeAll
    static void beforeAll() {
        ociEmulator.start();
    }

    @BeforeEach
    void beforeEach() {
        CreateBucketDetails bucketDetails = CreateBucketDetails.builder()
                .compartmentId(ociEmulator.getCompartmentId())
                .name(configuration.getBucket())
                .build();

        client.createBucket(CreateBucketRequest.builder()
                .namespaceName(configuration.getNamespace())
                .createBucketDetails(bucketDetails)
                .build());
    }

    void afterEach() {
        client.deleteBucket(DeleteBucketRequest.builder()
                .namespaceName(configuration.getNamespace())
                .bucketName(configuration.getBucket())
                .build());
    }

    @AfterAll
    static void afterAll() {
        ociEmulator.stop();
    }

    @Override
    protected void assertThatFileIsStored(String key, String expected) throws IOException {
        GetObjectResponse response = client.getObject(GetObjectRequest.builder()
                .bucketName(configuration.getBucket())
                .namespaceName(configuration.getNamespace())
                .objectName(key)
                .build());

        assertEquals(expected, textFromFile(response.getInputStream()));
    }

    @Singleton
    @Requires(property = "spec.name", value = SPEC_NAME)
    static class ObjectStorageListener implements BeanCreatedEventListener<ObjectStorageClient> {

        @Override
        public ObjectStorageClient onCreated(@NonNull BeanCreatedEvent<ObjectStorageClient> event) {
            ObjectStorageClient client = event.getBean();
            client.setEndpoint(ociEmulator.getEndpoint());
            return client;
        }
    }

    @Factory
    @Requires(property = "spec.name", value = SPEC_NAME)
    static class OciEmulatorAuthenticationFactory {

        @Bean
        @Singleton
        public SimpleAuthenticationDetailsProvider authenticationDetailsProvider() {
            return SimpleAuthenticationDetailsProvider.builder()
                    .tenantId(ociEmulator.getTenantId())
                    .userId(ociEmulator.getUserId())
                    .fingerprint(ociEmulator.getFingerprint())
                    .privateKeySupplier(ociEmulator.getPrivateKeySupplier())
                    .region(ociEmulator.getRegion())
                    .build();
        }

    }
}
