package example.micronaut;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import io.micronaut.aws.sdk.v1.EnvironmentAWSCredentialsProvider;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import jakarta.inject.Singleton;

import java.util.Optional;

import static com.amazonaws.regions.Regions.US_EAST_1;

@Factory
@Requires(env = Environment.TEST)
public class TestSqsClientFactory {

    @Singleton
    AmazonSQS sqsClient(Environment environment) {
        Optional<String> endpointOverride = environment.getProperty("aws.sqs.endpoint-override", String.class);

        return AmazonSQSClientBuilder
                .standard()
                .withCredentials(new EnvironmentAWSCredentialsProvider(environment))
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(endpointOverride.orElseThrow(() -> new IllegalStateException()), US_EAST_1.getName()))
                .build();
    }
}
