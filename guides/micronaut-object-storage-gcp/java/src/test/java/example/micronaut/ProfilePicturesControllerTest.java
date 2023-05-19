package example.micronaut;

import com.google.cloud.NoCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.objectstorage.googlecloud.GoogleCloudStorageConfiguration;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

import static example.micronaut.ProfilePicturesControllerTest.SPEC_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers(disabledWithoutDocker = true)
@MicronautTest
@Property(name = "spec.name", value = SPEC_NAME)
class ProfilePicturesControllerTest extends AbstractProfilePicturesControllerTest {

    static final String SPEC_NAME = "ProfilePicturesControllerTest";

    @Container
    static FakeGcsServerContainer fakeGcs = new FakeGcsServerContainer();

    @Inject
    Storage client;

    @Inject
    GoogleCloudStorageConfiguration configuration;

    @BeforeEach
    void beforeEach() {
        client.create(BucketInfo.newBuilder(configuration.getBucket()).build());
    }

    @AfterEach
    void afterEach() {
        client.get(configuration.getBucket()).delete();
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
            return StorageOptions.newBuilder()
                    .setHost(fakeGcs.getUrl())
                    .setProjectId("test-project")
                    .setCredentials(NoCredentials.getInstance())
                    .build()
                    .getService();
        }

    }
}
