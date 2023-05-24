package example.micronaut;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import io.micronaut.aws.sdk.v1.EnvironmentAWSCredentialsProvider;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.env.Environment;
import jakarta.inject.Singleton;

@Factory  // <1>
public class SqsClientFactory {

    @Singleton
    AmazonSQS sqsClient(Environment environment) {  // <2>
        return AmazonSQSClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)  // <3>
                .withCredentials(new EnvironmentAWSCredentialsProvider(environment))  // <4>
                .build();
    }
}