package example.micronaut;

import com.google.cloud.NoCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.gcp.GoogleCloudConfiguration;
import io.micronaut.objectstorage.googlecloud.GoogleCloudStorageConfiguration;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

import static example.micronaut.ProfilePicturesControllerTest.SPEC_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers(disabledWithoutDocker = true)
@MicronautTest
@Property(name = "spec.name", value = SPEC_NAME)
class ProfilePicturesControllerTest extends AbstractProfilePicturesControllerTest {

    static final String SPEC_NAME = "ProfilePicturesControllerTest";

    static FakeGcsServerContainer fakeGcs = new FakeGcsServerContainer(FakeGcsServerContainer.DEFAULT_IMAGE_NAME);

    @Inject
    Storage client;

    @Inject
    GoogleCloudStorageConfiguration configuration;

    @BeforeAll
    static void beforeAll() {
        fakeGcs.start();
    }

    @BeforeEach
    void beforeEach() {
        client.create(BucketInfo.newBuilder(configuration.getBucket()).build());
    }

    void afterEach() {
        client.get(configuration.getBucket()).delete();
    }

    @AfterAll
    static void afterAll() {
        fakeGcs.stop();
    }

    @Override
    protected void assertThatFileIsStored(String key, String expected) throws IOException {
        BlobId blobId = BlobId.of(configuration.getBucket(), key);
        Blob blob = client.get(blobId);

        assertEquals(expected, new String(blob.getContent()));
    }

    @Factory
    @Requires(property = "spec.name", value = SPEC_NAME)
    static class FakeGcsFactory {

        @Singleton
        @Primary
        Storage storage() {
            String fakeGcsExternalUrl = String.format("http://%s:%s", fakeGcs.getHost(), fakeGcs.getPort());
            return StorageOptions.newBuilder()
                    .setHost(fakeGcsExternalUrl)
                    .setProjectId("test-project")
                    .setCredentials(NoCredentials.getInstance())
                    .build()
                    .getService();
        }

    }
}
