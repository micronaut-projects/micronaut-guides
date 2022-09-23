package example.micronaut;

import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.model.CreateBucketDetails;
import com.oracle.bmc.objectstorage.requests.CreateBucketRequest;
import com.oracle.bmc.objectstorage.requests.DeleteBucketRequest;
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
    protected boolean assertThatFileIsStored(String key, String text) throws IOException {
        return false;
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
