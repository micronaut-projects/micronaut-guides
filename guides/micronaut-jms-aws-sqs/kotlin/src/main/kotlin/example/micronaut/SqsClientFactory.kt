/*
 * Copyright 2017-2024 original authors
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

import com.amazonaws.regions.Regions
import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.AmazonSQSClientBuilder
import io.micronaut.aws.sdk.v1.EnvironmentAWSCredentialsProvider
import io.micronaut.context.annotation.Factory
import io.micronaut.context.env.Environment
import jakarta.inject.Singleton

@Factory  // <1>
class SqsClientFactory {

    @Singleton
    fun sqsClient(environment: Environment): AmazonSQS {  // <2>
        return AmazonSQSClientBuilder
            .standard()
            .withRegion(Regions.US_EAST_1)  // <3>
            .withCredentials(EnvironmentAWSCredentialsProvider(environment))  // <4>
            .build()
    }
}