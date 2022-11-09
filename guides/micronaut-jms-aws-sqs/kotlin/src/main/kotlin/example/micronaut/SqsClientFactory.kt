package example.micronaut

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import com.amazonaws.regions.Regions
import io.micronaut.aws.sdk.v1.EnvironmentAWSCredentialsProvider
import io.micronaut.context.annotation.Factory
import io.micronaut.context.env.Environment
import jakarta.inject.Singleton

@Factory
class SqsClientFactory {

    @Singleton
    fun sqsClient(environment: Environment?): AmazonSQS {
        return AmazonSQSClientBuilder
            .standard()
            .withRegion(Regions.US_EAST_1)
            .withCredentials(EnvironmentAWSCredentialsProvider(environment))
            .build()
    }
}