/*
 * Copyright 2017-2023 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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