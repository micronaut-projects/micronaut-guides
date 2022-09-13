package example.micronaut

import io.micronaut.context.env.Environment
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.CreateBucketRequest
import spock.lang.AutoCleanup
import spock.lang.Shared

@MicronautTest(environments = Environment.AMAZON_EC2)
class AwsUploadControllerSpec extends AbstractUploadControllerSpec implements TestPropertyProvider {

    public static final String BUCKET_NAME = "profile-pictures-bucket"

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

}
