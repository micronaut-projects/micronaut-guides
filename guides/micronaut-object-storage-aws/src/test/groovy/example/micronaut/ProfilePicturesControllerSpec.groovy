package example.micronaut

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import spock.lang.AutoCleanup
import spock.lang.Shared

@MicronautTest
class ProfilePicturesControllerSpec extends AbstractProfilePicturesControllerSpec implements TestPropertyProvider {

    public static final String BUCKET_NAME = "micronaut-guide-object-storage"

    @Shared
    @AutoCleanup
    public LocalStackContainer localstack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:1.0.3"))
            .withServices(LocalStackContainer.Service.S3)

    @Inject
    S3Client s3

    @Override
    Map<String, String> getProperties() {
        localstack.start()
        return [
                "aws.accessKeyId": localstack.getAccessKey(),
                "aws.secretKey": localstack.getSecretKey(),
                "aws.region": localstack.getRegion(),
                "aws.s3.endpoint-override": localstack.getEndpointOverride(LocalStackContainer.Service.S3)

        ]
    }

    void setup() {
        s3.createBucket(CreateBucketRequest.builder().bucket(BUCKET_NAME).build() as CreateBucketRequest)
    }

    @Override
    boolean assertThatFileIsStored(String key, String text) {
        s3.getObject(GetObjectRequest.builder().key(key).bucket(BUCKET_NAME).build()).text == text
    }
}
