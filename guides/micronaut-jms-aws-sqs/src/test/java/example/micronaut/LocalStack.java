package example.micronaut;

import io.micronaut.core.util.CollectionUtils;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.Map;

public class LocalStack {
    private static final String TAG = "1.1.0";
    private static final String IMAGE = "localstack/localstack";
    private static LocalStackContainer localstack;

    private static final String DEMO_QUEUE = "demo_queue";

    static LocalStackContainer getLocalStack() {
        init();
        return localstack;
    }

    private static String getEndpoint() {
        return getLocalStack().getEndpointOverride(LocalStackContainer.Service.SQS).toString();
    }

    private static String secretAccessKey() {
        return getLocalStack().getSecretKey();
    }

    private static String getRegion() {
        return getLocalStack().getRegion();
    }

    private static String accessKeyId() {
        return getLocalStack().getAccessKey();
    }

    public static Map<String, String> getProperties() {
        return CollectionUtils.mapOf(
                "aws.accessKeyId", accessKeyId(),
                "aws.secretKey", secretAccessKey(),
                "aws.region", getRegion(),
                "aws.sqs.endpoint-override", getEndpoint());
    }

    public static void init() {
        if (localstack == null) {
            localstack = new LocalStackContainer(DockerImageName.parse(IMAGE + ":" + TAG))
                    .withServices(LocalStackContainer.Service.SQS);
            localstack.start();
            setupPrerequisites();
        }
    }

    public static void setupPrerequisites() {
        try {
            localstack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", DEMO_QUEUE);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException("Failed to setup the Localstack prerequisites", e);
        }
    }

    public static void close() {
        if (localstack != null) {
            localstack.close();
        }
    }
}
