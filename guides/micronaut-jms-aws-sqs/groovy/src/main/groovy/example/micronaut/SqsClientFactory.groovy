package example.micronaut

import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import groovy.transform.CompileStatic
import io.micronaut.aws.sdk.v1.EnvironmentAWSCredentialsProvider
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import jakarta.inject.Singleton


@CompileStatic
@Factory  // <1>
@Requires(notEnv = Environment.TEST)
class SqsClientFactory {

    @Singleton
    AmazonSQS sqsClient(Environment environment) {  // <2>
        AmazonSQSClientBuilder
                .standard()
                .withRegion(Regions.US_EAST_1)  // <3>
                .withCredentials(new EnvironmentAWSCredentialsProvider(environment))  // <4>
                .build()
    }
}
