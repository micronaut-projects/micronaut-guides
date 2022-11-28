package example.micronaut

import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import groovy.transform.CompileStatic
import io.micronaut.aws.sdk.v1.EnvironmentAWSCredentialsProvider
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.env.Environment
import jakarta.inject.Singleton

@CompileStatic
@Factory
class TestSqsClientFactory {

    @Singleton
    @Replaces(AmazonSQS.class)
    AmazonSQS sqsClient(Environment environment) {  // <2>
        Optional<String> endpointOverride = environment.getProperty("aws.sqs.endpoint-override", String.class);

        AmazonSQSClientBuilder
                .standard()
                .withCredentials(new EnvironmentAWSCredentialsProvider(environment))
                .withEndpointConfiguration(
                        new AwsClientBuilder.EndpointConfiguration(endpointOverride.orElseThrow(() -> new IllegalStateException()), Regions.US_EAST_1.getName()))
                .build();
    }
}
