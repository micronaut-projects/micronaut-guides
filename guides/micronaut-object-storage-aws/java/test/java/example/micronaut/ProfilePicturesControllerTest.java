package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Testcontainers(disabledWithoutDocker = true)
@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(LocalStackExtension.class)
class ProfilePicturesControllerTest extends AbstractProfilePicturesControllerTest implements TestPropertyProvider {
    public static final String BUCKET_NAME = "micronaut-guide-object-storage";

    @Inject
    S3Client s3;

    @BeforeEach
    void setup() {
        s3.createBucket(CreateBucketRequest.builder().bucket(BUCKET_NAME).build());
    }

    @AfterEach
    void cleanup() {
        s3.deleteBucket(DeleteBucketRequest.builder().bucket(BUCKET_NAME).build());
    }

    @Override
    @NonNull
    public Map<String, String> getProperties() {
        return LocalStack.getProperties();
    }

    @Override
    protected boolean assertThatFileIsStored(String key, String text) throws IOException {
        ResponseInputStream<GetObjectResponse> inputStream = s3.getObject(GetObjectRequest.builder().key(key).bucket(BUCKET_NAME).build());
        return text.equals(textFromFile(inputStream));
    }
}
