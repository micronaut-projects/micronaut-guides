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

import io.micronaut.context.event.BeanCreatedEvent
import io.micronaut.context.event.BeanCreatedEventListener
import io.micronaut.core.annotation.NonNull
import jakarta.inject.Singleton
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClientBuilder

@Singleton // <1>
class SqsClientBuilderListener implements BeanCreatedEventListener<SqsClientBuilder> { // <2>

    private final SqsConfig sqsConfig

    SqsClientBuilderListener(SqsConfig sqsConfig) { // <3>
        this.sqsConfig = sqsConfig
    }

    @Override
    SqsClientBuilder onCreated(@NonNull BeanCreatedEvent<SqsClientBuilder> event) {
        SqsClientBuilder builder = event.bean
        try {
            return builder
                .endpointOverride(new URI(sqsConfig.sqs.endpointOverride))
                .credentialsProvider(
                    StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(sqsConfig.accessKeyId, sqsConfig.secretKey)
                    )
                )
                .region(Region.of(sqsConfig.region))
        } catch (URISyntaxException e) {
            throw new RuntimeException(e)
        }
    }
}
