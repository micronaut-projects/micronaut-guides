package example.micronaut

import jakarta.inject.Singleton
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import io.micronaut.aws.sdk.v1.EnvironmentAWSCredentialsProvider
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.regions.Regions
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.context.env.Environment
import java.lang.IllegalStateException


@Factory
class TestSqsClientFactory {

    @Singleton
    @Replaces(AmazonSQS::class)
    fun sqsClient(environment: Environment): AmazonSQS {
        val endpointOverride = environment.getProperty("aws.sqs.endpoint-override", String::class.java)
        return AmazonSQSClientBuilder
            .standard()
            .withCredentials(EnvironmentAWSCredentialsProvider(environment))
            .withEndpointConfiguration(
                EndpointConfiguration(
                    endpointOverride.orElseThrow { IllegalStateException() },
                    Regions.US_EAST_1.getName()
                )
            )
            .build()
    }
}